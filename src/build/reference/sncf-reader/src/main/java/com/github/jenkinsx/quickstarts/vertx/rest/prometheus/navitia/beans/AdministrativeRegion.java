package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

public class AdministrativeRegion extends Named implements PlaceEmbeddedType {
	public String insee;
	public String label;
	public Coord coord;
	public int level;
	public String zip_code;
}
