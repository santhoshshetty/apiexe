package dfte.apiexe.helper;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dfte.apiexe.model.EnvsAndSuites;
import dfte.apiexe.utils.ExcelConstants;

public class GetRunnerDetails {

	private final Logger LOG = LoggerFactory.getLogger(GetRunnerDetails.class);

	public ArrayList<EnvsAndSuites> readDetails(String filelocation, String detailsType) throws Exception {
		DataFormatter formatter = new DataFormatter();
		XSSFWorkbook workbook = null;
		String fileLocation = filelocation;
		FileInputStream in = new FileInputStream(new File(fileLocation));
		workbook = new XSSFWorkbook(in);
		XSSFSheet sheet = workbook.getSheet(ExcelConstants.apiExeBaseSheet);

		ArrayList<EnvsAndSuites> execObjList = new ArrayList<EnvsAndSuites>();
		if (detailsType.equals(ExcelConstants.suitesdetails)) {
			int i = Integer.valueOf(ExcelConstants.apiExesuitesEntry);
			int suiteCol = Integer.valueOf(ExcelConstants.apiExesuiteRecord);
			ArrayList<String> suites = new ArrayList<String>();
			while (i <= sheet.getLastRowNum()) {
				EnvsAndSuites execObj = new EnvsAndSuites();
				String suiteName = formatter.formatCellValue(sheet.getRow(i).getCell(suiteCol));
				suites.add(suiteName);
				if (suiteName == null || suiteName.isEmpty()) {

				} else {
					execObj.setCode(suiteName);
					execObj.setName(suiteName);
					execObjList.add(execObj);
				}
				i++;
			}
		} else if (detailsType.equals(ExcelConstants.envdetails)) {
			int i = Integer.valueOf(ExcelConstants.apiExeenvironmentsEntry);
			int envCodeCol = Integer.valueOf(ExcelConstants.apiExeenvironmentCode);
			int envNameCol = Integer.valueOf(ExcelConstants.apiExeenvironmentName);
			while (i <= sheet.getLastRowNum()) {
				EnvsAndSuites execObj = new EnvsAndSuites();
				String envCode = formatter.formatCellValue(sheet.getRow(i).getCell(envCodeCol));
				String envName = formatter.formatCellValue(sheet.getRow(i).getCell(envNameCol));
				if (envCode == null || envCode.isEmpty()) {

				} else {
					execObj.setCode(envCode);
					execObj.setName(envName);
					execObjList.add(execObj);
				}
				i++;
			}

		}
		workbook.close();
		return execObjList;
	}

	public void readProjectDetails(XSSFSheet sheet, DataFormatter formatter) {
		int[] location;
		location = getCellPosition(ExcelConstants.apiExeproject);
		String project = formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1]));
		location = getCellPosition(ExcelConstants.apiExebuild);
		String buildNo = formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1]));
		location = getCellPosition(ExcelConstants.apiExeparallelExecution);
		String executeInParallel = formatter.formatCellValue(sheet.getRow(location[0]).getCell(location[1]));
		LOG.info("Project is: " + project + "<br>");
		LOG.info("Build No is: " + buildNo + "<br>");
		LOG.info("Execution in Parallel is: " + executeInParallel + "<br>");
	}

	public int[] getCellPosition(String value) {
		int[] cellPosition = new int[2];
		cellPosition[0] = Integer.parseInt(value.split(",")[0].trim());
		cellPosition[1] = Integer.parseInt(value.split(",")[1].trim());
		return cellPosition;
	}
}
