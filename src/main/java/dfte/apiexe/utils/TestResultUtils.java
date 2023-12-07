package dfte.apiexe.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestResultUtils {
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest logger;

	private final static Logger LOG = LoggerFactory.getLogger(TestResultUtils.class);

	public static void extentReportInitialize(String extentReportPath, String reportTitle, String reportName,
			String hostName, String environment, String testSuite) {
		htmlReporter = new ExtentHtmlReporter(extentReportPath.concat("/extentReport.html"));
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", hostName);
		extent.setSystemInfo("Environment", environment);
		extent.setSystemInfo("Test Suite", testSuite);
		htmlReporter.config().setDocumentTitle(reportTitle);
		htmlReporter.config().setReportName("API Test Executor");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.loadXMLConfig(
				"C:\\Users\\skumargoureshetty\\Documents\\GE\\Results_APIExec\\Results\\extent-config.xml");

	}

	public static void extentTestInitialize(String testName) {
		logger = extent.createTest(testName);
		LocalReportManager.setLogger(logger);
	}

	public static void closeExtentReport() {
		extent.flush();
	}

}
