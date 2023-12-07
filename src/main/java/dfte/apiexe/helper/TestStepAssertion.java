package dfte.apiexe.helper;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class TestStepAssertion {

	String validatorType;
	HashMap<String, ArrayList<AssertionRecord>> assertionObj = new HashMap<String, ArrayList<AssertionRecord>>();
}
