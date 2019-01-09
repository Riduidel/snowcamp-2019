package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses;

import java.util.LinkedList;
import java.util.List;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Route;

public class RoutesResponse extends NavitiaBaseResponse<RoutesResponse> {
	public List<Route> routes = new LinkedList<>();

	@Override
	public RoutesResponse append(RoutesResponse next) {
		routes.addAll(next.routes);
		return super.append(next);
	}
	
	
}
