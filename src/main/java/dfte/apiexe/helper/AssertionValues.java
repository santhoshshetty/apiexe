package dfte.apiexe.helper;

import lombok.Data;

@Data
public class AssertionValues {

	String assertOnField;

	String expected;

	String actual;
}
