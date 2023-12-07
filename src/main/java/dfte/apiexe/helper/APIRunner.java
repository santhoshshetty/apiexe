package dfte.apiexe.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dfte.apiexe.model.Exec;

public class APIRunner {

	private final Logger LOG = LoggerFactory.getLogger(APIRunner.class);

	public void runTest(Exec executionParams) {
		try {
			GenerateExecutionXML xmlGen = new GenerateExecutionXML();
			ProjectParams params = new ProjectParams();
			LOG.info("Execution File: " + executionParams.getApiexefile() + "<br>");
			LOG.info("Test Data Location is: " + executionParams.getTestdatafolder() + "<br>");
			LOG.info("Reports Location is: " + executionParams.getTestresultsfolder() + "<br>");
			LOG.info("Build No is: " + executionParams.getBuildnumber() + "<br>");
			LOG.info("Execution in Parallel is: " + "Sequential" + "<br>");
			LOG.info("Environment - URL: " + executionParams.getEnvironment() + "<br>");
			LOG.info("Suites to run: " + executionParams.getSuites() + "<br>");
			params.setProjectParams(executionParams.getApiexefile(), executionParams.getTestdatafolder(),
					executionParams.getTestresultsfolder(), executionParams.getBuildnumber(), "FL",
					executionParams.getEnvironment());
			xmlGen.buildXML(executionParams.getTestresultsfolder(), "No", executionParams.getSuites());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
