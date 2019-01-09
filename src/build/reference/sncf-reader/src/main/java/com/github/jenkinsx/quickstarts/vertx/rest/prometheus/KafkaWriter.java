package com.github.jenkinsx.quickstarts.vertx.rest.prometheus;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.serialization.JsonObjectSerializer;

public class KafkaWriter extends AbstractVerticle {
	private static final Logger logger = LoggerFactory
			.getLogger(KafkaWriter.class.getName());
	private KafkaProducer<String, JsonObject> producer;
	private Router router;

	public KafkaWriter(Router router) {
		this.router = router;
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		logger.info(String.format("starting %s", getClass().getName()));
		JsonObject userConfig = defaultConfig();
		Map<String, String> config = toMapOfStrings(userConfig);
		producer = KafkaProducer.create(vertx, config);
		producer.exceptionHandler(this::onKafkaException);
		vertx.eventBus().consumer(SncfReader.SNCF_READER_SCHEDULES, this::onScheduleReceived);
		// Now producer is ready, push content in
		logger.info(String.format("started %s", getClass().getName()));
		startFuture.complete();
	}
	
	public void onScheduleReceived(Message<JsonObject> message) {
		JsonObject body = message.body();
		KafkaProducerRecord<String, JsonObject> record = KafkaProducerRecord.create(config().getString("SNCF_READER_TOPIC_SCHEDULE", "sncfReaderSchedule"), body);
		producer.write(record, done -> {
			if(done.succeeded()) {
				logger.debug("Written route schedule "+body.encodePrettily());
			} else  {
				logger.warn("Unable to write route schedule "+body.encodePrettily(), done.cause());
			}
		});
	}

	private void resolvePathOf(JsonObject usedConfig, String pathToResolve, Collection<String> potentialPrefixes) {
		if(usedConfig.containsKey(pathToResolve)) {
			String unresolvedValue = usedConfig.getString(pathToResolve);
			if(!(new File(unresolvedValue).exists())) {
				logger.debug(String.format("The path used for %s doesn't exist : %s", pathToResolve, unresolvedValue));
				for(String prefix : potentialPrefixes) {
					File tested = new File(prefix+"/"+unresolvedValue);
					if(tested.exists()) {
						usedConfig.put(pathToResolve, tested.getAbsolutePath());
					}
				}
			}
		}
	}
	
	public void onKafkaException(Throwable throwable) {
		logger.fatal("Something bad happend on Kafka side. We should give up now", throwable);
		vertx.close();
	}
	
	private Map<String, String> toMapOfStrings(JsonObject json) {
		Map<String, Object> ofAnything = json.getMap();
		Map<String, String> returned = new TreeMap<>();
		for(Map.Entry<String, Object> entry : ofAnything.entrySet()) {
			Object value = entry.getValue();
			String key = entry.getKey();
			if(value instanceof String) {
				returned.put(key, (String) value);
			} else if(value instanceof Number) {
				returned.put(key, ((Number) value).toString());
			} else if(value instanceof Boolean) {
				returned.put(key, ((Boolean) value).toString());
			} else if(value instanceof JsonObject) {
				Map<String, String> contained = toMapOfStrings((JsonObject) value);
				for(Map.Entry<String, String> containedEntry : contained.entrySet()) {
					returned.put(key+"."+containedEntry.getKey(), containedEntry.getValue());
				}
			} else {
				throw new UnsupportedOperationException(String.format("Can't transform a %s to have it in map of strings", value.getClass().getName()));
			}
		}
		return returned;
	}
	private JsonObject defaultConfig() {
		return new JsonObject()
				.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config().getString("SNCF_READER_KAFKA_BOOTSTRAP_SERVER"))
				.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName())
				.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonObjectSerializer.class.getName())
				.put(ProducerConfig.CLIENT_ID_CONFIG, vertx.deploymentIDs().toString())
//				.put("group.id", "my_group")
//				.put("auto.offset.reset", "earliest")
				.put(ProducerConfig.RETRIES_CONFIG, 10)
				.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true)
				.put(ProducerConfig.ACKS_CONFIG, "all")
				;
	}
	
	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		producer.end();
		super.stop(stopFuture);
	}
}
