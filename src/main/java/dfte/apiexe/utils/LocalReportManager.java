package dfte.apiexe.utils;

import com.aventstack.extentreports.ExtentTest;

public class LocalReportManager {

	private static ThreadLocal<ExtentTest> loggerObj = new ThreadLocal<ExtentTest>();

	public static ExtentTest getLogger() {
		return loggerObj.get();
	}

	public static void setLogger(ExtentTest logger) {
		loggerObj.set(logger);
	}

}
