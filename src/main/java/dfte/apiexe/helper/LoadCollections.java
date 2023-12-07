package dfte.apiexe.helper;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dfte.apiexe.utils.APIConstants;
import dfte.apiexe.utils.ExcelConstants;
import dfte.apiexe.utils.GenerateFakeData;
import dfte.apiexe.utils.TestResultUtils;

public class LoadCollections {

	private final Logger LOG = LoggerFactory.getLogger(LoadCollections.class);
	private XSSFWorkbook workbook = null;
	String baseProjectPath = System.getProperty("user.dir");
	GlobalParams globalObj = new GlobalParams();
	APIExecutor exeuctor = new APIExecutor();
	TestCollection testCollectionObj = new TestCollection();
	TestSuite testSuitesObj = new TestSuite();
	TestSteps testStepsObj = new TestSteps();
	GenerateFakeData fakedata = new GenerateFakeData();

	@BeforeSuite
	public void beforeSuiteExec() throws Exception {
		TestResultUtils.extentReportInitialize(ProjectParams.reportsLocation, "Test", "Test", "Test", "Test", "Test");

	}

	@Test
	@Parameters({ "suite" })
	public void readSuiteForExecution(String suite) throws Exception {
		try {
			DataFormatter formatter = new DataFormatter();
			FileInputStream in = new FileInputStream(new File(ProjectParams.execFilePath));
			workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheet(suite);
			setGlobalParams(formatter, sheet);
			int i = Integer.parseInt(ExcelConstants.apiExetestsEntry) - 1;
			LinkedHashMap<String, List<TestCases>> allTestCasesObj = null;
			List<TestCases> testCasesListObj = new ArrayList<TestCases>();
			while (i < sheet.getLastRowNum()) {
				i++;
				String executeTestCase = (String) getValueOrNull(
						getParam(i, ExcelConstants.apiExeexecuteTC, formatter, sheet));
				if (executeTestCase.equals("Yes")) {
					TestCases testCasesObj = new TestCases();
					testCasesObj.setTestCaseName(formatter
							.formatCellValue(sheet.getRow(i).getCell(Integer.parseInt(ExcelConstants.apiExetestCase))));
					testCasesObj.setTestCaseId(generateKey());
					boolean flag = false;
					List<TestSteps> testStepsListObj = new ArrayList<TestSteps>();
					TestSteps testStepsObj;
					LinkedHashMap<String, String> paramsMap = null;
					LinkedHashMap<String, List<TestSteps>> steps;
					String data;
					while (!flag) {
						testStepsObj = new TestSteps();
						testStepsObj.setTestStepId(generateKey());
						data = (String) getValueOrNull(
								getParam(i, ExcelConstants.apiExetestStepKeyword, formatter, sheet));
						testStepsObj.setStepKeyword(data);
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExebddStep, formatter, sheet));
						testStepsObj.setBddStep(data);
						data = (String) getValueOrNull(
								getParam(i, ExcelConstants.apiExegherkinKeyword, formatter, sheet));
						testStepsObj.setGherkinKeyword(data);
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExemethod, formatter, sheet));
						testStepsObj.setMethod(data);
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExeendPoint, formatter, sheet));
						testStepsObj.setEndPoint(data);
						data = (String) getValueOrNull(
								getParam(i, ExcelConstants.apiExeheaderParams, formatter, sheet));
						if (data != null) {
							paramsMap = getMapForParams(data);
							testStepsObj.setHeaderParams(paramsMap);
						} else {
							testStepsObj.setHeaderParams(null);
						}
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExequeryParams, formatter, sheet));
						if (data != null) {
							paramsMap = getMapForParams(data);
							testStepsObj.setQueryParams(paramsMap);
						} else {
							testStepsObj.setQueryParams(null);
						}
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExepathParams, formatter, sheet));
						if (data != null) {
							paramsMap = getMapForParams(data);
							testStepsObj.setPathParams(paramsMap);
						} else {
							testStepsObj.setPathParams(null);
						}
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExebodyParams, formatter, sheet));
						String body = null;
						if (data != null)
							body = getPayLoadFromFile(ProjectParams.testDataLocation.concat("\\" + data));
						testStepsObj.setBodyParams(body);
						data = (String) getValueOrNull(getParam(i, ExcelConstants.apiExestatus, formatter, sheet));
						testStepsObj.setStatus(data);
						data = (String) getValueOrNull(
								getParam(i, ExcelConstants.apiExecaptureValue, formatter, sheet));
						testStepsObj.setCaptureValue(data);
						data = (String) getValueOrNull(
								getParam(i, ExcelConstants.apiExeassertionValue, formatter, sheet));
						if (data != null) {
							TestStepAssertion stepAssert = extractAssertionValues(data);
							testStepsObj.setStepAssertion(stepAssert);
						} else {
							testStepsObj.setAssertionValues(null);
						}
						LOG.info("Reading API Data: Test Step Object: " + testStepsObj + "<br>");
						testStepsListObj.add(testStepsObj);
						if (i == sheet.getLastRowNum()) {
							i++;
							flag = true;
						} else {
							i++;
							flag = checkIfRowEmpty(sheet.getRow(i));
						}
					}
					steps = new LinkedHashMap<String, List<TestSteps>>();
					steps.put(testCasesObj.getTestCaseName(), testStepsListObj);
					testCasesObj.setTestStepsObj(steps);
					testCasesListObj.add(testCasesObj);
					LOG.info("Reading API Data: Test Case Object: " + testCasesObj + "<br>");
				} else {
					LOG.info("Reading API Data: Test case execution is set to No" + "<br>");
					i = getNextEmptyRow(sheet, i);
				}
			}
			allTestCasesObj = new LinkedHashMap<String, List<TestCases>>();
			allTestCasesObj.put(testSuitesObj.getTestSuiteName(), testCasesListObj);
			testSuitesObj.setTestCasesObj(allTestCasesObj);
			LOG.info("Reading API Data: Test Suite Object: " + testSuitesObj + "<br>");
			LOG.info("****API Execution - START****" + "<br>");
			exeuctor.testSuiteExecution(testSuitesObj, globalObj);
		} catch (Exception ex) {
			LOG.error("Error occurred in reading the data" + "<br>");
			ex.printStackTrace();
		}
	}

	public int getNextEmptyRow(XSSFSheet sheet, int i) {
		while (i < sheet.getLastRowNum()) {
			boolean emptyRow = checkIfRowEmpty(sheet.getRow(i));
			if (emptyRow == true) {
				break;
			}
			i++;
		}
		return i;
	}

	public String getParam(int location, String key, DataFormatter formatter, XSSFSheet sheet) {
		try {
			String returnValue = formatter.formatCellValue(sheet.getRow(location).getCell(Integer.parseInt(key)));
			return returnValue;
		} catch (Exception ex) {

		}
		return null;
	}

	public void setGlobalParams(DataFormatter formatter, XSSFSheet sheet) {
		try {
			int[] location;
			location = getCellPosition(ExcelConstants.apiExeEnvironment);
			globalObj.setEnvironment(formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1])));
			location = getCellPosition(ExcelConstants.apiExebaseURI);
			String envURL = formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1]));
			if (envURL == " " || envURL == null || envURL.isEmpty()) {
				globalObj.setUrl(ProjectParams.envrinonmentURL);
			} else {
				globalObj.setUrl(formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1])));
			}
			location = getCellPosition(ExcelConstants.apiExefeature);
			testCollectionObj.setFeatureName(formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1])));
			location = getCellPosition(ExcelConstants.apiExetestSuite);
			testSuitesObj.setTestSuiteName(formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1])));
			testSuitesObj.setTestSuiteId(generateKey());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public long generateKey() {
		Random random = new Random();
		long key = random.nextInt(100000);
		System.out.println(key);
		return key;
	}

	public String getPayLoadFromFile(String path) {
		try {

			String payLoad = new String(Files.readAllBytes(Paths.get(path)));
			return payLoad;
		} catch (Exception ex) {
			return null;
		}
	}

	public Object getValueOrNull(String prop) {
		if (prop == null || prop == "") {
			return null;
		}
		return prop;
	}

	public Object getValueOrNullForParams(String prop) {
		if (prop == null || prop == "") {
			return null;
		}
		return getMapForParams(prop);
	}

	public LinkedHashMap<String, String> getMapForParams(String parameters) {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
		try {
			long count = parameters.chars().filter(ch -> ch == '|').count();
			if (count == 0) {
				params.put(parameters.split("=")[0].trim(), parameters.split("=")[1].trim());
			} else {
				StringTokenizer tokens = new StringTokenizer(parameters, "|");
				while (tokens.hasMoreTokens()) {
					String value = tokens.nextToken();
					System.out.println("Assertion Token is: " + value);
					String key = value.split("=")[0].trim();
					String keyValue = value.split("=")[1].trim();
					if (keyValue.contains(APIConstants.fakeData)) {
						keyValue = keyValue.replace('{', ' ').replace('}', ' ').trim();
						keyValue = fakedata.getData(keyValue);
					} else {
						System.out.println("Fake Data not required");
					}
					params.put(key, keyValue);
				}
			}
		} catch (Exception ex) {

		}
		return params;
	}

	public TestStepAssertion extractAssertionValues(String assertionRecord) {
		TestStepAssertion stepAssertion = new TestStepAssertion();
		ArrayList<AssertionRecord> listRecords = new ArrayList<AssertionRecord>();
		HashMap<String, ArrayList<AssertionRecord>> records = new HashMap<String, ArrayList<AssertionRecord>>();
		StringTokenizer strToken;
		strToken = new StringTokenizer(assertionRecord, "-");
		String record = strToken.nextToken();
		String value = strToken.nextToken();
		strToken = new StringTokenizer(value, ";");
		while (strToken.hasMoreTokens()) {
			LinkedHashMap<String, AssertionValues> assertVal = new LinkedHashMap<String, AssertionValues>();
			AssertionRecord assertRecord = new AssertionRecord();
			StringTokenizer str = new StringTokenizer(strToken.nextToken(), "|");
			AssertionValues assertValues = new AssertionValues();
			String assertType = str.nextToken();
			assertValues.setAssertOnField(str.nextToken());
			assertValues.setExpected(str.nextToken());
			assertValues.setActual(str.nextToken());
			assertVal.put(assertType, assertValues);
			assertRecord.setAssertType(assertType);
			assertRecord.setAssertionRecord(assertVal);
			listRecords.add(assertRecord);
		}
		records.put(record, listRecords);
		stepAssertion.setValidatorType(record);
		stepAssertion.setAssertionObj(records);
		return stepAssertion;
	}

	public int[] getCellPosition(String value) {
		int[] cellPosition = new int[2];
		cellPosition[0] = Integer.parseInt(value.split(",")[0].trim());
		cellPosition[1] = Integer.parseInt(value.split(",")[1].trim());
		return cellPosition;
	}

	public boolean checkIfRowEmpty(Row row) {
		try {
			for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				if (cell != null && cell.getCellType() != CellType.BLANK) {
					return false;
				}
			}
		} catch (Exception ex) {

		}
		return true;
	}
}
