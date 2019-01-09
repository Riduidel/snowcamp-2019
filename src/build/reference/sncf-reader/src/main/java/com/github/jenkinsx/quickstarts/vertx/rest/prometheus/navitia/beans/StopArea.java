package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

public class StopArea extends Named implements PlaceEmbeddedType {
	public String label;
	public Coord coord;
	public Collection<AdministrativeRegion> administrative_regions = new LinkedList<>();
	public Collection<StopPoint> stop_points = new LinkedList<>();
	public Collection<StopAreaCode> codes = new LinkedList<>();
	public Collection<Link> links = new LinkedList<>();
	public String timezone;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StopArea [");
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (label != null) {
			builder.append("label=");
			builder.append(label);
		}
		builder.append("]");
		return builder.toString();
	}
}
