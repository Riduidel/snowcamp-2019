package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @see doc.navitia.io/#line
 * @author nicolas-delsaux
 *
 */
@JsonIgnoreProperties(value= {"geojson"})
public class Line extends Named {
	public String code;
	public Collection<LineCode> codes = new LinkedList<>();
	public Network network;
	public String color;
	public String text_color;
	// TODO convert to valid hour
	public String opening_time;
	// TODO convert to valid hour
	public String closing_time;
	public Collection<Route> routes = new LinkedList<>();
	public CommercialMode commercial_mode;
	public Collection<PhysicalMode> physical_modes = new LinkedList<>();
	public Collection<Link> links = new LinkedList<>();
}
