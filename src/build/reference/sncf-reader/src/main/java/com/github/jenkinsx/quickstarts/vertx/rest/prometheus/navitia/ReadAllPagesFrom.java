package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.NavitiaBaseResponse;

import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadAllPagesFrom<Type extends NavitiaBaseResponse> implements Callback<Type> {
	private static final Logger logger = LoggerFactory.getLogger(ReadAllPagesFrom.class.getName());

	private Type accumulator;
	private Collection<Handler<Type>> callbacks = new LinkedList<>();
	private Function<Integer, Call<Type>> function;
	
	public ReadAllPagesFrom(Function<Integer, Call<Type>> function) {
		this.function = function;
	}

	public ReadAllPagesFrom(Function<Integer, Call<Type>> function, Type accumulated) {
		this.function = function;
		this.accumulator = accumulated;
	}

	@Override
	public void onResponse(Call<Type> call, Response<Type> response) {
		Type currentCallResponse = response.body();
		if(response.code()<300) {
			if(currentCallResponse.pagination.isOnLastPage()) {
				logger.debug(String.format("Loaded ALL pages !"));
				callbacks.stream().forEach(c -> c.handle(currentCallResponse));
			} else {
				logger.debug(String.format("Loaded page %s", currentCallResponse.pagination.start_page));
				Call<Type> next = function.apply(currentCallResponse.pagination.start_page+1);
				next.enqueue(new ReadAllPagesFrom(
						function, 
						accumulator==null ? currentCallResponse : accumulator.append(currentCallResponse))
							.then(callbacks));
			}
		} else {
			logger.error(String.format("Unable to perform HTTP call due to error %d. message is \"%s\"", response.code(), response.message()));
		}
	}
	
	public ReadAllPagesFrom<Type> then(Collection<Handler<Type>> handler) {
		this.callbacks.addAll(handler);
		return this;
	}
	
	public ReadAllPagesFrom<Type> then(Handler<Type> handler) {
		this.callbacks.add(handler);
		return this;
	}

	@Override
	public void onFailure(Call<Type> call, Throwable t) {
		logger.error("Call to Navitia API failed...", t);
	}

	public void enqueue() {
		Call<Type> next = function.apply(0);
		next.enqueue(new ReadAllPagesFrom(function, accumulator).then(callbacks));
	}
	
}