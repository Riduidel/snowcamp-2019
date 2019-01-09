package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.beans;

public class Pagination {
	public int start_page;
	public int items_on_page;
	public int items_per_page;
	public int total_result;
	public boolean isOnLastPage() {
		return items_on_page<items_per_page;
	}
	public void append(Pagination pagination) {
		start_page = Math.min(start_page, pagination.start_page);
		items_on_page += pagination.items_on_page;
		total_result = Math.max(total_result, pagination.total_result);
	}
}
