package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses;

import java.util.Collection;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Note;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.RouteSchedule;

public class RouteScheduleResponse extends NavitiaBaseResponse<RouteScheduleResponse> {
	public Collection<RouteSchedule> route_schedules = new LinkedList<>();
	public Collection<Note> notes = new LinkedList<>();
	@JsonIgnore
	public Collection<String> exceptions;
	@Override
	public RouteScheduleResponse append(RouteScheduleResponse next) {
		route_schedules.addAll(next.route_schedules);
		notes.addAll(next.notes);
		return super.append(next);
	}
}
