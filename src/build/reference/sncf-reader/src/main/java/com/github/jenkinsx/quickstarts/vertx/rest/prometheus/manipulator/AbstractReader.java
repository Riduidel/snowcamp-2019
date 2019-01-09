package com.github.jenkinsx.quickstarts.vertx.rest.prometheus.manipulator;

import java.util.Optional;

import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.NavitiaApi;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.ReadAllPagesFrom;
import com.github.jenkinsx.quickstarts.vertx.rest.prometheus.navitia.responses.NavitiaBaseResponse;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import retrofit2.Call;

public abstract class AbstractReader<ReadType extends NavitiaBaseResponse<ReadType>> implements Reader<ReadType> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractReader.class.getName());
	protected final NavitiaApi api;
	protected final String token;
	private Optional<ReadType> read = Optional.empty();
	protected final JsonObject config;

	public AbstractReader(NavitiaApi api, String token, JsonObject config) {
		super();
		this.api = api;
		this.token = token;
		this.config = config;
	}
	
	@Override
	public ReadAllPagesFrom<ReadType> read() {
		logger.info(String.format("Starting basic reading of informations for %s", getClass().getSimpleName()));
		setRead(Optional.empty());
		ReadAllPagesFrom<ReadType> reader = new ReadAllPagesFrom<ReadType>(this::processPage)
				.then(list -> this.onDownloaded(list));
		reader.enqueue();
		return reader;
	}

	protected abstract Call<ReadType> processPage(int page);

	protected void onDownloaded(ReadType list) {
		logger.info(String.format("Finished basic reading of informations for %s", getClass().getSimpleName()));
		setRead(Optional.of(list));
	}

	protected <Type extends NavitiaBaseResponse<Type>> void respondRead(HttpServerResponse response,
			Optional<Type> value) {
		if (value.isPresent()) {
			respondRead(response, value.get());
		} else {
			response.end("Nothing to download here ...");
		}
	}

	protected <Type extends NavitiaBaseResponse<Type>> void respondRead(HttpServerResponse response,
			Object value) {
		response.putHeader("content-type", "application/json; charset=utf-8").end(Json.encode(value));
	}

	public Optional<ReadType> getRead() {
		return read;
	}

	public void setRead(Optional<ReadType> read) {
		this.read = read;
	}

}