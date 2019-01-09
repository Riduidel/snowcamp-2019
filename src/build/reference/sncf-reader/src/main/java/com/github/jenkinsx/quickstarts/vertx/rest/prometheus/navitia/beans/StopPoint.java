package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

public class StopPoint extends Named implements PlaceEmbeddedType {
	public Coord coord;
	public Collection<AdministrativeRegion> administrative_regions = new LinkedList<>();
	public Collection<String> equipments = new LinkedList<>();
	public Collection<CommercialMode> commercial_modes = new LinkedList<>();
	public Collection<Link> links = new LinkedList<>();
	public Collection<PhysicalMode> physical_modes = new LinkedList<>();
	public StopArea stop_area;
	public String label;
	public FareZone fare_zone;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StopPoint [");
		if (stop_area != null) {
			builder.append("stop_area=");
			builder.append(stop_area);
		}
		builder.append("]");
		return builder.toString();
	}
}
