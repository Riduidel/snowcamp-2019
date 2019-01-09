package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Line;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Place;

public class LinesResponse extends NavitiaBaseResponse<LinesResponse> {
	public Collection<Line> lines = new LinkedList<>();

	public LinesResponse append(LinesResponse linesList) {
		lines.addAll(linesList.lines);
		return super.append(linesList);
	}

	public Set<Place> extractPlaces() {
		Set<Place> places = lines.stream()
			.flatMap(line -> line.routes.stream())
			.map(r -> r.direction)
			.collect(Collectors.toCollection(() -> new TreeSet<Place>()));
		return places;
	}
}
