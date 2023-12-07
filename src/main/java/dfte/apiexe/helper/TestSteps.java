package dfte.apiexe.helper;

import java.util.LinkedHashMap;

import lombok.Data;

@Data
public class TestSteps {
	long testStepId;
	String bddStep;
	String gherkinKeyword;
	String method;
	String endPoint;
	String bodyParams;
	String status;
	String captureValue;
	String assertionType;
	String stepKeyword;
	LinkedHashMap<String, String> headerParams = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> queryParams = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> pathParams = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> assertionValues = new LinkedHashMap<String, String>();
	TestStepAssertion stepAssertion;
}
