package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.manipulator;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.SncfReader;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.NavitiaApi;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Route;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.RouteSchedule;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.RouteScheduleResponse;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.RoutesResponse;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutesReader extends AbstractReader<RoutesResponse> {
	private static final Logger logger = LoggerFactory.getLogger(RoutesReader.class.getName());
	
	private Map<String, RouteSchedule> schedules = new TreeMap<>();

	private EventBus eventBus;

	public RoutesReader(NavitiaApi api2, String token2, JsonObject config, EventBus eventBus) {
		super(api2, token2, config);
		this.eventBus = eventBus;
	}

	@Override
	protected Call<RoutesResponse> processPage(int page) {
		return api.routes(token, page);
	}

	/**
	 * For each route, download the route schedule !
	 */
	@Override
	protected void onDownloaded(RoutesResponse list) {
		super.onDownloaded(list);
		logger.info("Downloaded routes !");
		if(config.containsKey("SNCF_READER_LIMIT_ROUTES_DOWNLOAD")) {
			for (int i = 0; i < config.getInteger("SNCF_READER_LIMIT_ROUTES_DOWNLOAD", 10); i++) {
				readRouteSchedule(list.routes.get(i));
			}
		} else {
			list.routes.forEach(route -> readRouteSchedule(route));
		}
	}

	private void readRouteSchedule(Route route) {
		String routeId = route.id;
		Call<RouteScheduleResponse> schedule = api.routeSchedule(token, route.id);
		logger.info(String.format("Fetching route schedule for %s", route));
		schedule.enqueue(new Callback<RouteScheduleResponse>() {

			@Override
			public void onResponse(Call<RouteScheduleResponse> call, Response<RouteScheduleResponse> response) {
				Collection<RouteSchedule> route_schedules = response.body().route_schedules;
				if(route_schedules.size()>1) {
					throw new UnsupportedOperationException("Can't handle multiple schedules for one route");
				}
				route_schedules.forEach(r -> {
					schedules.put(route.id, r);
					eventBus.publish(SncfReader.SNCF_READER_SCHEDULES, JsonObject.mapFrom(r));
					logger.info(String.format("Published schedule %s", r.display_informations.name));
				});
			}

			@Override
			public void onFailure(Call<RouteScheduleResponse> call, Throwable t) {
				logger.error(String.format("unable to fetch route schedule for route id %s\nYou can try it manually with\nGET https://{{token}}@api.navitia.io/v1/coverage/sncf/routes/%s/route_schedules ", routeId, routeId), t);
			}
		});
	}
	
	@Override
	public void setRead(Optional<RoutesResponse> read) {
		schedules.clear();
		super.setRead(read);
	}

	@Override
	public void contributeRoutes(Router router) {
		router.route("/sncf/routes").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			if (routingContext.queryParams().contains("reload")) {
				read().then(_not -> respondRead(response, this.getRead()));
			} else {
				respondRead(response, this.getRead());
			}
		});
		router.route("/sncf/routes/:id/schedule").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			String id = routingContext.pathParam("id");
			if(schedules.containsKey(id)) {
				respondRead(response, schedules.get(id));
			} else {
				response.setStatusCode(500).setStatusMessage(String.format("Can't find a schedule for %s", id)).end();
			}
		});
	}
}