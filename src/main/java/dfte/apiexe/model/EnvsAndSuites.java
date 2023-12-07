package dfte.apiexe.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class EnvsAndSuites implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public EnvsAndSuites() {

	}

	String code;

	String name;
}
