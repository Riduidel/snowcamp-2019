package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Disruption {
	public DisruptionStatus status;
	public String disruption_id;
	public Severity severity;
	public String impact_id;
	public Collection<Period> application_periods = new LinkedList<>();
	public Collection<Message> messages = new LinkedList<>();
	public String updated_at;
	public String uri;
	@JsonIgnore
	public Collection<String> impacted_objects = new LinkedList<>();
	public String disruption_uri;
	public String contributor;
	public String cause;
	public String id;
}
