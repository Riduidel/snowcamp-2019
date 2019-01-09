package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value= {"geojson"})
public class Route extends Named {
	public Line line;
	public Place direction;
	public Collection<Link> links = new LinkedList<>();
	public boolean is_frequence;
	public DirectionType direction_type;
}
