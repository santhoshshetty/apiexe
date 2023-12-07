package dfte.apiexe.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class Exec implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Exec() {

	}

	@NotNull
	String testresultsfolder;

	String testdatafolder;

	String buildnumber;

	@NotNull
	String environment;

	@NotNull
	ArrayList<String> suites;

	@NotNull
	String apiexefile;
}
