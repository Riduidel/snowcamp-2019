package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

public class PublicTransportDateTime {
	public String date_time;
	public Collection<String> additional_informations = new LinkedList<>();
	public String base_date_time;
	public DataFreshness data_freshness;
	public Collection<Link> links = new LinkedList<>();
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PublicTransportDateTime [");
		if (date_time != null) {
			builder.append("date_time=");
			builder.append(date_time);
		}
		builder.append("]");
		return builder.toString();
	}
}
