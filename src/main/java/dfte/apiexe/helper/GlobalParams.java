package dfte.apiexe.helper;

import java.util.HashMap;

import lombok.Data;

@Data
public class GlobalParams {

	String url;
	String environment;
	HashMap<String, String> globalParams = new HashMap<String, String>();
}
