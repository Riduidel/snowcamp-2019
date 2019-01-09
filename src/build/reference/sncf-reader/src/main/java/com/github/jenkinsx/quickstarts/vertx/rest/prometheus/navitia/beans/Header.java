package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

public class Header {
	public DisplayInformations display_informations;
	public Collection<String> additional_informations = new LinkedList<>();
	public Collection<Link> links = new LinkedList<>();
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Header [");
		if (display_informations != null) {
			builder.append("display_informations=");
			builder.append(display_informations);
		}
		builder.append("]");
		return builder.toString();
	}
}
