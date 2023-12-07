package dfte.apiexe.utils;

import java.util.HashMap;

import dfte.apiexe.helper.APIExecutionConstants;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredAPIBuilder {
	private RequestSpecBuilder builder = new RequestSpecBuilder();
	private String method;
	private String url;
	private Response response;

	public Response getResponse() {
		return response;
	}

	public RestAssuredAPIBuilder(String method, String uri) {
		this.url = uri;
		this.method = method;
	}

	public RestAssuredAPIBuilder setBodyParams(String bodyParams) {
		builder.setBody(bodyParams);
		return this;
	}

	public RestAssuredAPIBuilder setRequestParams(String reqParams) {
		builder.setBody(reqParams);
		return this;
	}

	public RestAssuredAPIBuilder setRequestParams(HashMap<String, String> reqParams, String reqParamType) {
		switch (reqParamType) {
		case APIExecutionConstants.headerParams:
			builder.addHeaders(reqParams);
			return this;
		case APIExecutionConstants.queryParams:
			builder.addQueryParams(reqParams);
			return this;
		case APIExecutionConstants.pathParams:
			builder.addPathParams(reqParams);
			return this;
		}
		return this;
	}

	public RestAssuredAPIBuilder executeAPI() {
		RequestSpecification requestSpec = builder.build();
		RequestSpecification request = RestAssured.given();
		request.spec(requestSpec);
		switch (this.method) {
		case APIConstants.GET:
			response = request.get(this.url);
			return this;
		case APIConstants.POST:
			response = request.contentType(ContentType.JSON).post(this.url);
			return this;
		case APIConstants.DELETE:
			response = request.delete(this.url);
			return this;
		case APIConstants.PUT:
			response = request.contentType(ContentType.JSON).put(this.url);
			return this;
		case APIConstants.PATCH:
			response = request.contentType(ContentType.JSON).patch(this.url);
			return this;
		}
		return null;
	}
}
