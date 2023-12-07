package dfte.apiexe.helper;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;

@Data
public class TestCollection {

	String featureName;
	LinkedHashMap<String, List<TestSuite>> featureObj = new LinkedHashMap<String, List<TestSuite>>();
}
