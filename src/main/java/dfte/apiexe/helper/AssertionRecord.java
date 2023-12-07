package dfte.apiexe.helper;

import java.util.LinkedHashMap;

import lombok.Data;

@Data
public class AssertionRecord {

	String assertType;
	LinkedHashMap<String, AssertionValues> assertionRecord = new LinkedHashMap<String, AssertionValues>();
}
