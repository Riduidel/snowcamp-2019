package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Network extends Named {
	public Collection<NetworkCode> codes = new LinkedList<>();
	public Collection<Link> links = new LinkedList<>();
}
