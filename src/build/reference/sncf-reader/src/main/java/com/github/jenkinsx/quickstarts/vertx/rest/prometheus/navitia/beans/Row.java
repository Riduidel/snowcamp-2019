package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

public class Row {
	public StopPoint stop_point;
	public Collection<PublicTransportDateTime> date_times = new LinkedList<>();
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Row [");
		if (stop_point != null) {
			builder.append("stop_point=");
			builder.append(stop_point);
			builder.append(", ");
		}
		if (date_times != null) {
			builder.append("date_times=");
			builder.append(date_times);
		}
		builder.append("]");
		return builder.toString();
	}
}
