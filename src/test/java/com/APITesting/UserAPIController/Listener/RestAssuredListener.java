package com.APITesting.UserAPIController.Listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.APITesting.UserAPIController.TestCase.ApiRequest;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestAssuredListener implements Filter{
	private static final Logger logger = LogManager.getLogger(RestAssuredListener.class);

	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext ctx) {
		
		Response response =ctx.next(requestSpec, responseSpec);
		logger.info("\n Request Method " +requestSpec.getMethod()+
				     "\n Request URL "+requestSpec.getURI()+
				     "\n Request Body " +requestSpec.getBody()+
				     "\n Response Body " +response.getBody().asPrettyString()+
				     "\n Rsponse code " +response.getStatusCode());
		return response;
	}

}
