package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

public class POI extends Named implements PlaceEmbeddedType {
	public String label;
	public POIType poi_type;
	public Stands stands;
}
