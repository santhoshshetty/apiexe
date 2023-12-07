package dfte.apiexe.helper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatLogRecords {

	public String format(String logRecord) {
		String formattedstr = null;
		int[] arr = checkIfMatched(logRecord);
		if (arr != null) {
			int start = arr[0];
			int end = arr[1];
			int n = logRecord.length();
			String endstr = logRecord.substring(end + 1, n);
			String startstr = logRecord.substring(start, end);
			formattedstr = "<b><u>".concat(startstr).concat("</b></u>").concat(endstr);
			formattedstr = "<div>".concat(formattedstr).concat("</div>");
		}
		return formattedstr;
	}

	public int[] checkIfMatched(String checkRecord) {
		ArrayList<String> records = getComparisonRecords();
		int[] logindices = new int[2];
		for (String comp : records) {
			String regex = APIExecutionConstants.logregex.concat(comp).trim();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(checkRecord);
			while (matcher.find()) {
				logindices[0] = matcher.start();
				logindices[1] = matcher.end();
				return logindices;
			}
		}
		return null;
	}

	public ArrayList<String> getComparisonRecords() {
		ArrayList<String> matchingClass = new ArrayList<String>();
		matchingClass.add(APIExecutionConstants.apiexeapplication);
		matchingClass.add(APIExecutionConstants.apiexecutor);
		matchingClass.add(APIExecutionConstants.apiexeloadcollections);
		matchingClass.add(APIExecutionConstants.apiexerunner);
		return matchingClass;
	}
}
