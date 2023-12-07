package dfte.apiexe.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class GenerateExecutionXML {

	public void buildXML(String reportsLoc, String parallelExec, ArrayList<String> suitesForExecution) {
		TestNG myTestNG = new TestNG();
		XmlSuite mySuite = new XmlSuite();
		mySuite.setName("API Execution");
		if (parallelExec.equals("Yes")) {
			mySuite.setParallel(XmlSuite.ParallelMode.TESTS);
		} else {
			mySuite.setParallel(XmlSuite.ParallelMode.NONE);
		}
		List<XmlTest> myTests = new ArrayList<XmlTest>();
		HashMap<String, String> params;
		ArrayList<String> suitesList = suitesForExecution;
		XmlTest myTest;
		for (String element : suitesList) {
			params = new HashMap<String, String>();
			params.put("suite", element);
			myTest = new XmlTest(mySuite);
			myTest.setName(element);
			myTest.setParameters(params);
			List<XmlClass> myClasses = new ArrayList<XmlClass>();
			myClasses.add(new XmlClass("dfte.apiexe.helper.LoadCollections"));
			myTest.setClasses(myClasses);
			myTest.setThreadCount(0);
			myTests.add(myTest);
		}
		mySuite.setTests(myTests);
		List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		mySuites.add(mySuite);
		myTestNG.setXmlSuites(mySuites);
		mySuite.setFileName("temp.xml");
		if (parallelExec.equals("Yes"))
			mySuite.setThreadCount(suitesList.size());
		createXmlFile(mySuite, reportsLoc);
		myTestNG.run();
	}

	public static void createXmlFile(XmlSuite Suite, String location) {
		FileWriter writer;
		try {
			writer = new FileWriter(new File(location.concat("\\APIExecution.xml")));
			writer.write(Suite.toXml());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
