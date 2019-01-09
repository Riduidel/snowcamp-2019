package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Place extends Named {
	public int quality;
	@JsonIgnore
	public PlaceEmbeddedType embedded_type;
	public StopArea stop_area;
}
