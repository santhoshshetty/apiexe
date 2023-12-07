package dfte.apiexe.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.path.json.JsonPath;

public class AssertionValidator {

	private final Logger LOG = LoggerFactory.getLogger(AssertionValidator.class);

	public HashMap<Boolean, ArrayList<String>> validateAssertions(TestSteps steps, String response) {
		TestStepAssertion assertion = steps.getStepAssertion();
		assertion.getValidatorType();
		HashMap<Boolean, ArrayList<String>> execResult = new HashMap<Boolean, ArrayList<String>>();
		boolean assertObjStatus = true;
		ArrayList<String> results = new ArrayList<String>();
		switch (assertion.getValidatorType()) {
		case "R2PayLoad":
			int size = assertion.getAssertionObj().get(assertion.getValidatorType()).size();
			for (int i = 0; i < size; i++) {
				AssertionRecord record = assertion.getAssertionObj().get(assertion.getValidatorType()).get(i);
				String expJsonPath = record.getAssertionRecord().get(record.getAssertType()).getExpected();
				String actJsonPath = record.getAssertionRecord().get(record.getAssertType()).getActual();
				String assertOnObj = record.getAssertionRecord().get(record.getAssertType()).getAssertOnField();
				Object expValue = JsonPath.from(steps.getBodyParams()).get(expJsonPath);
				Object actValue = JsonPath.from(response).get(actJsonPath);
				boolean isSuccess = validateAssertions(record.getAssertType(), assertOnObj, actValue, expValue);
				if (isSuccess) {
					results.add(assertOnObj + ":Pass");
				} else {
					results.add(assertOnObj + ":Fail");
					assertObjStatus = false;
				}
			}
			execResult.put(assertObjStatus, results);
			return execResult;
		case "R2Params":
			int r2pSize = assertion.getAssertionObj().get(assertion.getValidatorType()).size();
			for (int i = 0; i < r2pSize; i++) {
				AssertionRecord record = assertion.getAssertionObj().get(assertion.getValidatorType()).get(i);
				String expParam = record.getAssertionRecord().get(record.getAssertType()).getExpected();
				String actJsonPath = record.getAssertionRecord().get(record.getAssertType()).getActual();
				expParam = expParam.replace('{', ' ').trim().replace('}', ' ').trim();
				String assertOnObj = record.getAssertionRecord().get(record.getAssertType()).getAssertOnField();
				Object actValue = JsonPath.from(response).get(actJsonPath);
				Object expValue = extractValueInParams(steps, expParam);
				boolean isSuccess = validateAssertions(record.getAssertType(), assertOnObj, actValue, expValue);
				if (isSuccess) {
					results.add(assertOnObj + ":Pass");
				} else {
					results.add(assertOnObj + ":Fail");
					assertObjStatus = false;
				}
			}
			execResult.put(assertObjStatus, results);
			return execResult;

		case "R2Data":
			int r2DSize = assertion.getAssertionObj().get(assertion.getValidatorType()).size();
			for (int i = 0; i < r2DSize; i++) {
				AssertionRecord record = assertion.getAssertionObj().get(assertion.getValidatorType()).get(i);
				String actJsonPath = record.getAssertionRecord().get(record.getAssertType()).getActual();
				Object expValue = record.getAssertionRecord().get(record.getAssertType()).getExpected();
				String assertOnObj = record.getAssertionRecord().get(record.getAssertType()).getAssertOnField();
				Object actValue = JsonPath.from(response).get(actJsonPath);
				boolean isSuccess = validateAssertions(record.getAssertType(), assertOnObj, actValue, expValue);
				if (isSuccess) {
					results.add(assertOnObj + ":Pass");
				} else {
					results.add(assertOnObj + ":Fail");
					assertObjStatus = false;
				}
			}
			execResult.put(assertObjStatus, results);
			return execResult;
		}
		return null;

	}

	public Object extractValueInParams(TestSteps steps, String val) {
		Object value = null;
		if (steps.getQueryParams() != null) {
			if (checkIfKeyExists(steps.getQueryParams(), val)) {
				value = steps.getQueryParams().get(val);
				return value;
			}
		}
		if (steps.getPathParams() != null) {
			if (checkIfKeyExists(steps.getPathParams(), val)) {
				value = steps.getPathParams().get(val);
				return value;
			}
		}
		return value;
	}

	public boolean checkIfKeyExists(LinkedHashMap<String, String> params, String val) {
		Optional<String> value = params.keySet().stream().filter(e -> e.equals(val)).findAny();
		if (value.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	public double getDoubleValues(Object ob) {
		double d = Double.valueOf(ob.toString()).doubleValue();
		return d;
	}

	public boolean validateAssertions(String compCheck, String assertObj, Object act, Object exp) {
		switch (compCheck) {
		case "SEC":
			if (exp.toString().equals(act.toString())) {
				return true;
			} else {
				return false;
			}
		case "SCC":
			if (act.toString().contains(exp.toString())) {
				return true;
			} else {
				return false;
			}
		case "NEC":
			if (getDoubleValues(exp) == getDoubleValues(act)) {
				return true;
			} else {
				return false;
			}
		case "NGC":
			if (getDoubleValues(act) > getDoubleValues(exp)) {
				return true;
			} else {
				return false;
			}
		case "NLC":
			if (getDoubleValues(act) < getDoubleValues(exp)) {
				return true;
			} else {
				return false;
			}
		case "NN":
			if (exp.toString().equals(act.toString())) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}
}
