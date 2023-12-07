package dfte.apiexe.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.Status;

import dfte.apiexe.utils.AssertionHelper;
import dfte.apiexe.utils.LocalReportManager;
import dfte.apiexe.utils.RestAssuredAPIBuilder;
import dfte.apiexe.utils.TestResultUtils;
import io.restassured.response.Response;

public class APIExecutor extends TestResultUtils {

	private final Logger LOG = LoggerFactory.getLogger(APIExecutor.class);
	AssertionHelper assertHelper = new AssertionHelper();
	boolean executionResult;
	Response response;

	ProgressBarStats stats = ProgressBarStats.getStatsInstance();

	public void testSuiteExecution(TestSuite testSuiteObj, GlobalParams globalObj) {
		try {
			stats.setTotalCases(testSuiteObj.getTestCasesObj().get(testSuiteObj.getTestSuiteName()).size());
			System.out.println(stats.getTotalTestCases());
			System.out.println(stats.getPassCases());
			System.out.println(stats.getFailCases());
			boolean testSuiteExecution = testCasesExeuction(
					testSuiteObj.getTestCasesObj().get(testSuiteObj.getTestSuiteName()), globalObj);
			if (testSuiteExecution) {
				LOG.info("Test Suite execution is SUCCESS" + "<br>");
			} else {
				LOG.info("Test Suite execution is FAIL" + "<br>");
			}
		} catch (Exception ex) {

		}
	}

	public boolean testCasesExeuction(List<TestCases> testCasesObj, GlobalParams globalObj) {
		boolean testSuiteExecResult = true;
		try {
			for (TestCases testCaseObj : testCasesObj) {
				globalObj.getGlobalParams().clear();
				extentTestInitialize(testCaseObj.testCaseName);
				LOG.info("Size of each testcase: "
						+ testCaseObj.getTestStepsObj().get(testCaseObj.getTestCaseName()).size() + "<br>");
				boolean testStepExec = testStepsExecution(
						testCaseObj.getTestStepsObj().get(testCaseObj.getTestCaseName()), globalObj);
				if (!testStepExec) {
					LOG.info("Current Test Case execution is failed" + "<br>");
					testSuiteExecResult = false;
					stats.incrementFailCases();
					System.out.println(stats.getFailCases());
					System.out.println(stats.getExecutionFailPercentage());

				} else {
					stats.incrementPassCases();
					System.out.println(stats.getPassCases());
					System.out.println(stats.getExecutionPassPercentage());
					LOG.info("Current Test Case execution is Pass" + "<br>");
				}
				closeExtentReport();
			}
		} catch (Exception ex) {
			return false;
		}
		return testSuiteExecResult;
	}

	public boolean testStepsExecution(List<TestSteps> testStepsObj, GlobalParams globalObj) {
		AssertionValidator validator = new AssertionValidator();
		try {
			for (TestSteps testStepObj : testStepsObj) {
				LOG.info("Test step in execution: " + testStepObj + "<br>");
				executionResult = true;
				response = executeAPI(testStepObj, globalObj);
				LOG.info("Response is: " + "<div>" + response.prettyPrint() + "</div>" + "<br>");
				boolean statusValidation = assertHelper.validateStatusResponse(testStepObj, response);
				if (statusValidation) {
					captureExecutionParams(testStepObj, globalObj, response);
					if (testStepObj.getStepAssertion() == null) {
						LocalReportManager.getLogger().log(Status.PASS, testStepObj.getBddStep() + " Success");
					} else {
						HashMap<Boolean, ArrayList<String>> stepAssertion = validator.validateAssertions(testStepObj,
								response.asString());
						boolean assertionResult = (boolean) stepAssertion.keySet().toArray()[0];
						if (!assertionResult) {
							LOG.info("Assertions are not matched.." + "<br>");
							LocalReportManager.getLogger().log(Status.FAIL, testStepObj.getBddStep()
									+ " Assertions not matched" + stepAssertion.get(assertionResult));
							return false;
						} else {
							LocalReportManager.getLogger().log(Status.PASS,
									testStepObj.getBddStep() + " Assertions: " + stepAssertion.get(assertionResult));
						}
					}
				} else {
					LOG.info("Assertions are not validated as the status is not expected.." + "<br>");
					logger.log(Status.FAIL,
							testStepObj.getBddStep() + "    Status not expected:  Expected Status is:   "
									+ testStepObj.getStatus() + "  Actual Status is: " + response.getStatusCode());
					return false;
				}
			}
		} catch (Exception ex) {
			LOG.error("Exception occurred in test step execution" + "<br>");
			logger.log(Status.FAIL, "Exception occurred");
			return false;
		}
		return true;
	}

	public void captureExecutionParams(TestSteps testStepObj, GlobalParams globalObj, Response response) {
		try {
			if (testStepObj.getCaptureValue() != null) {
				LoadCollections collectionObj = new LoadCollections();
				LinkedHashMap<String, String> captureValues = collectionObj
						.getMapForParams(testStepObj.getCaptureValue());
				for (String key : captureValues.keySet()) {
					Object responseCaptureParams = response.getBody().jsonPath().get(captureValues.get(key));
					globalObj.getGlobalParams().put(key, responseCaptureParams.toString());
				}
			}
		} catch (Exception ex) {
			LOG.error("Exception occurred in capturing execution params" + ex + "<br>");
		}
	}

	public Response executeAPI(TestSteps testStepObj, GlobalParams globalObj) {
		try {
			RestAssuredAPIBuilder apiBuilder = new RestAssuredAPIBuilder(testStepObj.getMethod(),
					globalObj.getUrl().concat(testStepObj.getEndPoint()));
			if (testStepObj.getHeaderParams() != null) {
				setrunTimeCaptureHeaderParams(testStepObj, globalObj);
				apiBuilder.setRequestParams(testStepObj.getHeaderParams(), APIExecutionConstants.headerParams);
			}
			if (testStepObj.getQueryParams() != null) {
				setrunTimeCaptureQueryParams(testStepObj, globalObj);
				apiBuilder.setRequestParams(testStepObj.getQueryParams(), APIExecutionConstants.queryParams);
			}
			if (testStepObj.getPathParams() != null) {
				setrunTimeCapturePathParams(testStepObj, globalObj);
				apiBuilder.setRequestParams(testStepObj.getPathParams(), APIExecutionConstants.pathParams);
			}
			if (testStepObj.getBodyParams() != null) {
				apiBuilder.setBodyParams(testStepObj.getBodyParams());
			}
			return apiBuilder.executeAPI().getResponse();
		} catch (Exception ex) {
			LOG.error("Exception occurred in API execution" + ex + "<br>");
		}
		return null;
	}

	public void setrunTimeCaptureHeaderParams(TestSteps testStepObj, GlobalParams globalObj) {
		try {
			for (String key : testStepObj.getHeaderParams().keySet()) {
				if (testStepObj.getHeaderParams().get(key).contains("{")) {
					String execParam = testStepObj.getHeaderParams().get(key);
					execParam = execParam.replace('{', ' ').trim().replace('}', ' ').trim();
					for (String globalKey : globalObj.getGlobalParams().keySet()) {
						if (execParam.contains(globalKey)) {
							String headerSetValue = execParam.replace(globalKey,
									globalObj.getGlobalParams().get(globalKey));
							testStepObj.getHeaderParams().put(key, headerSetValue.trim());
							break;
						}
					}

				} else {
					LOG.info("There are no run time captures" + "<br>");
				}
			}
		} catch (Exception ex) {
			LOG.error("Exception occurred in setting run time capture params to header params" + "<br>");
		}
	}

	public void setrunTimeCaptureQueryParams(TestSteps testStepObj, GlobalParams globalObj) {
		try {
			for (String key : testStepObj.getQueryParams().keySet()) {
				if (testStepObj.getQueryParams().get(key).contains("{")) {
					String execParam = testStepObj.getQueryParams().get(key);
					execParam = execParam.replace('{', ' ').trim().replace('}', ' ').trim();
					for (String globalKey : globalObj.getGlobalParams().keySet()) {
						if (globalKey.equalsIgnoreCase(execParam)) {
							testStepObj.getQueryParams().put(key, globalObj.getGlobalParams().get(globalKey));
							break;
						}
					}

				} else {
					LOG.info("There are no run time captures" + "<br>");
				}
			}
		} catch (Exception ex) {
			LOG.error("Exception occurred in setting run time capture params to Query params" + "<br>");
		}
	}

	public void setrunTimeCapturePathParams(TestSteps testStepObj, GlobalParams globalObj) {
		try {
			for (String key : testStepObj.getPathParams().keySet()) {
				if (testStepObj.getPathParams().get(key).contains("{")) {
					String execParam = testStepObj.getPathParams().get(key);
					execParam = execParam.replace('{', ' ').trim().replace('}', ' ').trim();
					for (String globalKey : globalObj.getGlobalParams().keySet()) {
						if (globalKey.equalsIgnoreCase(execParam)) {
							testStepObj.getPathParams().put(key, globalObj.getGlobalParams().get(globalKey));
							break;
						}
					}

				} else {
					LOG.info("There are no run time captures" + "<br>");
				}
			}
		} catch (Exception ex) {
			LOG.error("Exception occurred in setting run time capture params to Path params" + "<br>");
		}
	}

}