package com.github.jenkinsx.quickstarts.vertx.rest.prometheus;

import static io.prometheus.client.Counter.build;
import static io.vertx.core.Vertx.vertx;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.hotspot.DefaultExports;
import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

public class VertxRestPrometheusVerticle extends AbstractVerticle {

    private final CollectorRegistry registry = CollectorRegistry.defaultRegistry;

    private final Counter helloCounter = build("hello_count", "Number of incoming requests to /hello endpoint.").
                    register(registry);

    static final Logger logger = LoggerFactory.getLogger(VertxRestPrometheusVerticle.class.getName());

    @Override
    public void start(Future<Void> startFuture) throws Exception {
		ConfigRetriever configRetriever = loadConfig();
		configRetriever.getConfig(configLoaded -> {
			if(configLoaded.succeeded()) {
				// Add other verticles
				Router router = Router.router(vertx);
				
				DeploymentOptions options = new DeploymentOptions(new JsonObject().put("config", configLoaded.result()));
				logger.debug("Using as config "+configLoaded.result().encodePrettily());
				vertx.deployVerticle(new SncfReader(router), options);
				vertx.deployVerticle(new KafkaWriter(router), options);

				exposeHelloWorldEndpoint(router);
				exposeMetricsEndpoint(router);
				exposeHealthEndpoint(router);
				
				vertx.createHttpServer().requestHandler(router::accept).listen(8080);
				startFuture.complete();
			} else {
				startFuture.fail(configLoaded.cause());
			}
		});
    }

    private ConfigRetriever loadConfig() {
		ConfigRetrieverOptions options = new ConfigRetrieverOptions()
				.addStore(new ConfigStoreOptions()
						.setType("json").setConfig(config()))
				.addStore(new ConfigStoreOptions()
						.setType("env")
						.setOptional(true))
				.addStore(new ConfigStoreOptions()
						.setType("sys")
						.setOptional(true))
				;
		return ConfigRetriever.create(vertx, options);
	}

	private void exposeHelloWorldEndpoint(Router router) {
        router.route("/hello").handler(routingContext -> {
            helloCounter.inc();
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json");
            response.end(new JsonObject().put("hello", "world").toBuffer());
        });
    }

    private void exposeMetricsEndpoint(Router router) {
        DefaultExports.initialize();
        router.route("/metrics").handler(new MetricsHandler(registry));
    }

    private void exposeHealthEndpoint(Router router) {
        router.route("/actuator/health").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            response.end("OK");
        });
    }

    // IDE testing helper

    public static void main(String[] args) {
        vertx().deployVerticle(new VertxRestPrometheusVerticle());
    }

}