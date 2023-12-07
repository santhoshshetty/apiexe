package dfte.apiexe.helper;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;

@Data
public class TestSuite {

	String testSuiteName;
	long testSuiteId;
	LinkedHashMap<String, List<TestCases>> testCasesObj = new LinkedHashMap<String, List<TestCases>>();

}
