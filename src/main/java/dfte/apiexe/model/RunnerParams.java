package dfte.apiexe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class RunnerParams implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	HashMap<String, ArrayList<EnvsAndSuites>> runnerparams = new HashMap<String, ArrayList<EnvsAndSuites>>();

}
