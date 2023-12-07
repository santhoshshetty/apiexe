package dfte.apiexe.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

@Data
public class ExecLogRecords implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	ArrayList<String> logRecords;

	int passcases;

	int failcases;
}
