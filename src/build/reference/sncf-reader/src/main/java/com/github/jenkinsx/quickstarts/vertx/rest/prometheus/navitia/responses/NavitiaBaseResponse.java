package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses;

import java.util.Collection;
import java.util.LinkedList;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Context;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Disruption;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.FeedPublisher;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Link;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans.Pagination;

public abstract class NavitiaBaseResponse<Type extends NavitiaBaseResponse> {
	public Pagination pagination = new Pagination();
	public Collection<Link> links = new LinkedList<>();
	public Collection<Disruption> disruptions = new LinkedList<>();
	public Collection<FeedPublisher> feed_publishers = new LinkedList<>();
	public Context context = new Context();
	
	public Type append(Type next) {
		pagination.append(next.pagination);
		return (Type) this;
	}
}
