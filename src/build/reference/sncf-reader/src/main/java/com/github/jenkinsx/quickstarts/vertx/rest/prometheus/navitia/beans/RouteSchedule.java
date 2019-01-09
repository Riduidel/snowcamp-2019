package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

import org.geojson.GeoJsonObject;

public class RouteSchedule {
	public DisplayInformations display_informations;
	public Table table;
	public String additional_informations;
	public Collection<Link> links;
	public GeoJsonObject geojson;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RouteSchedule [");
		if (display_informations != null) {
			builder.append("display_informations=");
			builder.append(display_informations);
			builder.append(", ");
		}
		if (table != null) {
			builder.append("table=\n");
			builder.append(table);
		}
		builder.append("\n]");
		return builder.toString();
	}
}
