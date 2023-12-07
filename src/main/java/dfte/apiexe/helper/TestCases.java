package dfte.apiexe.helper;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;

@Data
public class TestCases {

	String testCaseName;
	long testCaseId;
	LinkedHashMap<String, List<TestSteps>> testStepsObj = new LinkedHashMap<String, List<TestSteps>>();

}
