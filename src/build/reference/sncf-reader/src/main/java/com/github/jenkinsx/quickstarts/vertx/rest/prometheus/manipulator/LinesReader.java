package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.manipulator;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.NavitiaApi;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.LinesResponse;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import retrofit2.Call;

public class LinesReader extends AbstractReader<LinesResponse> {
	private static final Logger logger = LoggerFactory.getLogger(LinesReader.class.getName());
	

	public LinesReader(NavitiaApi api, String token, JsonObject config) {
		super(api, token, config);
	}

	@Override
	protected Call<LinesResponse> processPage(int page) {
		return api.lines(token, page);
	}

	@Override
	public void contributeRoutes(Router router) {
		router.route("/sncf/lines").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			if (routingContext.queryParams().contains("reload")) {
				read().then(_not -> respondRead(response, getRead()));
			}
			respondRead(response, getRead());
		});
		router.route("/sncf/lines/places").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			if (getRead().isPresent()) {
				response.putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encode(getRead().get().extractPlaces()));
			} else {
				response.end("Downloading lines list and places");
			}
		});
	}

}