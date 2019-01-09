package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

import java.util.ArrayList;
import java.util.Collection;

public class Channel extends Named {
	public String content_type;
	public Collection<String> types = new ArrayList<>();
}
