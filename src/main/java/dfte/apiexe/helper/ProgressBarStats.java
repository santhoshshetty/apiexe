package dfte.apiexe.helper;

public class ProgressBarStats {

	private ProgressBarStats() {

	}

	static ProgressBarStats statsObj = new ProgressBarStats();

	public static ProgressBarStats getStatsInstance() {
		return statsObj;
	}

	int totaltestcases;

	int testcasespass;

	int testcasesfail;

	public int getTotalTestCases() {
		return totaltestcases;
	}

	public int getPassCases() {
		return testcasespass;
	}

	public int getFailCases() {
		return testcasesfail;
	}

	public void setTotalCases(int total) {
		totaltestcases = total;
	}

	public void setPassCases(int pass) {
		testcasespass = pass;
	}

	public void setFailCases(int fail) {
		testcasesfail = fail;
	}

	public void initializeCounts() {
		setTotalCases(0);
		setPassCases(0);
		setFailCases(0);
	}

	public void incrementPassCases() {
		setPassCases(getPassCases() + 1);
	}

	public void incrementFailCases() {
		setFailCases(getFailCases() + 1);
	}

	public int getExecutionPassPercentage() {
		if (getTotalTestCases() == 0) {
			return 0;
		} else {
			int percentage = getPassCases() * 100 / getTotalTestCases();
			return percentage;
		}
	}

	public int getExecutionFailPercentage() {
		if (getTotalTestCases() == 0) {
			return 0;
		} else {
			int percentage = getFailCases() * 100 / getTotalTestCases();
			return percentage;
		}
	}

}
