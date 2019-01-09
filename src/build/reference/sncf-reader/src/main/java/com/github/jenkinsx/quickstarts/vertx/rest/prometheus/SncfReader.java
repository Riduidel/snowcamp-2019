package com.github.jenkinsx.quickstarts.vertx.rest.prometheus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.manipulator.Reader;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.manipulator.RoutesReader;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.NavitiaApi;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.ReadAllPagesFrom;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.LinesResponse;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.NavitiaBaseResponse;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.RoutesResponse;
import com.julienviet.retrofit.vertx.VertxCallFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SncfReader extends AbstractVerticle {
	String token;
	private Router router;
	private HttpClient client;
	private Retrofit retrofit;
	NavitiaApi api;
	Optional<RoutesResponse> routes;
	private Map<Class<? extends NavitiaBaseResponse>, Reader> readers;
	public static final String SNCF_READER_SCHEDULES = "schedules";

	static final Logger logger = LoggerFactory.getLogger(SncfReader.class.getName());

	public SncfReader(Router router) {
		this.router = router;
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		EventBus eventBus = vertx.eventBus();
		token = config().getString("SNCF_READER_TOKEN");
		client = vertx.createHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		retrofit = new Retrofit.Builder().callFactory(new VertxCallFactory(client)).baseUrl("https://api.navitia.io")
				.addConverterFactory(JacksonConverterFactory.create(mapper)).build();
		api = retrofit.create(NavitiaApi.class);
		readers = new HashMap<>();
		readers.put(RoutesResponse.class, new RoutesReader(api, token, config(), eventBus));
		logger.debug("Initialized SNCF reader with config "+config().encodePrettily());
		if(config().getBoolean("SNCF_READER_READ_AT_STARTUP", false)) {
			logger.info("############\n"
					+ "############\n"
					+ "IMMEDIATLY DOWNLOADING INFOS !"
					+ "############\n"
					+ "############\n"
					);
			readers.values().parallelStream().forEach(r -> r.read());
		}

		exposeReadEndpoint(router);
		super.start(startFuture);
	}

	private void exposeReadEndpoint(Router router) {
		readers.values().stream().forEach(r -> r.contributeRoutes(router));
	}
}
