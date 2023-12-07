package dfte.apiexe.helper;

import lombok.Data;

@Data
public class EmailObj {

	String fromAddress;

	String toAddresses;

	String subject;

	String body;

	String execResultsPath;
}
