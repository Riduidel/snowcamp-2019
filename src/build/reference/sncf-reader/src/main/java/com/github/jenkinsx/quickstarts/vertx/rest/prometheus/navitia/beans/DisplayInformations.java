package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

public class DisplayInformations {
	public String direction;
	public String code;
	public String network;
	public Collection<Link> links = new LinkedList<>();
	public String color;
	public String label;
	public String commercial_mode;
	public String text_color;
	public String name;
	public String physical_mode;
	public String headsign;
	public Collection<String> equipments = new LinkedList<>();
	public String description;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DisplayInformations [");
		if (direction != null && !direction.isEmpty()) {
			builder.append("direction=");
			builder.append(direction);
			builder.append(", ");
		}
		if (code != null && !code.isEmpty()) {
			builder.append("code=");
			builder.append(code);
			builder.append(", ");
		}
		if (network != null && !network.isEmpty()) {
			builder.append("network=");
			builder.append(network);
			builder.append(", ");
		}
		if (links != null && !links.isEmpty()) {
			builder.append("links=");
			builder.append(links);
			builder.append(", ");
		}
		if (color != null && !color.isEmpty()) {
			builder.append("color=");
			builder.append(color);
			builder.append(", ");
		}
		if (label != null && !label.isEmpty()) {
			builder.append("label=");
			builder.append(label);
			builder.append(", ");
		}
		if (commercial_mode != null && !commercial_mode.isEmpty()) {
			builder.append("commercial_mode=");
			builder.append(commercial_mode);
			builder.append(", ");
		}
		if (text_color != null && !text_color.isEmpty()) {
			builder.append("text_color=");
			builder.append(text_color);
			builder.append(", ");
		}
		if (name != null && !name.isEmpty()) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (physical_mode != null && !physical_mode.isEmpty()) {
			builder.append("physical_mode=");
			builder.append(physical_mode);
		}
		builder.append("]");
		return builder.toString();
	}
}
