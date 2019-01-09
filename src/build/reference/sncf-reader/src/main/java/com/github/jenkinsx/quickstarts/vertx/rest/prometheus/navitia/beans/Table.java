package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Table {
	public Collection<Header> headers = new LinkedList<>();
	public Collection<Row> rows = new LinkedList<>();
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Table [");
		if (headers != null) {
			builder.append("headers=\n");
			builder.append(headers.stream().map(h -> h.toString()).collect(Collectors.joining("\n")));
			builder.append("\n, ");
		}
		if (rows != null) {
			builder.append("rows=\n");
			builder.append(rows.stream().map(r -> r.toString()).collect(Collectors.joining("\n")));
		}
		builder.append("]");
		return builder.toString();
	}
}
