package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;

public class Address extends Named implements PlaceEmbeddedType {
	public String label;
	public Coord coord;
	public int house_number;
	public Collection<AdministrativeRegion> administrative_regions;
}
