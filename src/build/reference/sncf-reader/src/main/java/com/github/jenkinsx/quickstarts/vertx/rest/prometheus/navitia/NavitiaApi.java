package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.LinesResponse;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.RouteScheduleResponse;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.RoutesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NavitiaApi {
	int DEFAULT_START_PAGE = 0;
	int DEFAULT_COUNT = 1000;

	public default Call<LinesResponse> lines(String token, int start_page) {
		return lines(token, start_page, DEFAULT_COUNT);
	}

	@GET("/v1/coverage/sncf/lines")
	Call<LinesResponse> lines(@Header("Authorization") String token, @Query("start_page") int start_page, @Query("count") int count);

	public default Call<RoutesResponse> routes(String token, int start_page) {
		return routes(token, start_page, DEFAULT_COUNT);
	}

	@GET("/v1/coverage/sncf/routes")
	Call<RoutesResponse> routes(@Header("Authorization") String token, @Query("start_page") int start_page, @Query("count") int count);


	@GET("/v1/coverage/sncf/routes/{route_id}/route_schedules")
	Call<RouteScheduleResponse> routeSchedule(@Header("Authorization") String token, @Path("route_id") String id);
}
