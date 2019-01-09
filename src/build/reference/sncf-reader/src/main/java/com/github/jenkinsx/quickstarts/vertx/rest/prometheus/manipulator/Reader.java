package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.manipulator;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.ReadAllPagesFrom;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.NavitiaBaseResponse;

import io.vertx.ext.web.Router;

public interface Reader<ReadType extends NavitiaBaseResponse<ReadType>> {
	public ReadAllPagesFrom<ReadType> read();

	public void contributeRoutes(Router router);

}