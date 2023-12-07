package dfte.apiexe.utils;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import dfte.apiexe.helper.TestSteps;
import io.restassured.response.Response;

public class AssertionHelper {

	public boolean validateStatusResponse(TestSteps testStepObj, Response response) {
		if (response.getStatusCode() == Integer.parseInt(testStepObj.getStatus())) {
			System.out.println("Status is as expected..");
		} else {
			return false;
		}
		return true;
	}

	public boolean assertResponse(Response response, String assertionType, String jsonPath, String value) {
		try {
			switch (assertionType) {
			case AssertionsConstants.StringEqualTo:
				response.then().body(jsonPath, is(value));
				break;
			case AssertionsConstants.NumberEqualTo:
				response.then().body(jsonPath, is(Integer.parseInt(value)));
				break;
			case AssertionsConstants.ContainsInAnyOrder:
				response.then().body(jsonPath, containsInAnyOrder("Java", "Javascript", "SpringBoot"));
				break;
			case "NotANullValue":
				response.then().body(jsonPath, notNullValue());
				break;
			case "hasLength":
				response.then().body(jsonPath, hasLength(2));
				break;
			case "blankOrNullString":
				response.then().body(jsonPath, blankOrNullString());
				break;
			case "arrayWithSize":
				response.then().body(jsonPath, arrayWithSize(3));
				break;
			default:
				System.out.println("Assertion is not found");
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
}
