package dfte.apiexe.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dfte.apiexe.helper.APIRunner;
import dfte.apiexe.helper.FormatLogRecords;
import dfte.apiexe.helper.GetRunnerDetails;
import dfte.apiexe.helper.ProgressBarStats;
import dfte.apiexe.model.EnvsAndSuites;
import dfte.apiexe.model.Exec;
import dfte.apiexe.model.ExecLogRecords;
import dfte.apiexe.model.RunnerParams;
import dfte.apiexe.utils.ExcelConstants;

@Service
public class RunAPIService {

	private final Logger LOG = LoggerFactory.getLogger(RunAPIService.class);

	ProgressBarStats stats = ProgressBarStats.getStatsInstance();

	public void runSuites(Exec exec) throws Exception {
		stats.initializeCounts();
		APIRunner runner = new APIRunner();
		runner.runTest(exec);
		// runner.runTest("C:\\Users\\skumargoureshetty\\Documents\\GE\\Results_APIExec\\Payloads",
		// "C:\\Users\\skumargoureshetty\\Documents\\GE\\Results_APIExec\\Results",
		// "C:\\Users\\skumargoureshetty\\Documents\\GE\\Results_APIExec\\APIExec\\APIExecutor.xlsm");
	}

	public ExecLogRecords returnExecLog() {
		ExecLogRecords logrecords = new ExecLogRecords();
		try {
			ArrayList<String> records = getLogRecords();
			if (records.isEmpty() || records == null) {
				logrecords.setLogRecords(null);
			}
			logrecords.setPasscases(stats.getExecutionPassPercentage());
			logrecords.setFailcases(stats.getExecutionFailPercentage());
			logrecords.setLogRecords(records);
			return logrecords;
		} catch (Exception ex) {
			System.out.println("Loggg: exception occurred");
			LOG.error("Exception occurred in reading execution log file");
			return null;
		}
	}

	public ArrayList<String> getLogRecords() {
		ArrayList<String> records = null;
		try {
			FormatLogRecords logrecords = new FormatLogRecords();
			records = new ArrayList<String>();
			File file = new File("C:\\Users\\skumargoureshetty\\Documents\\EGC\\src\\debug.log");
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				String record = read.nextLine();
				String formattedRecord = logrecords.format(record);
				if (formattedRecord == null) {
					records.add(record);
				} else {
					records.add(formattedRecord);
				}
			}
			read.close();
		} catch (Exception ex) {
			return null;
		}
		return records;
	}

	public RunnerParams getRunnerDetails(String filelocation) throws Exception {
		RunnerParams runner = new RunnerParams();
		ArrayList<EnvsAndSuites> env = getEnvironmentDetails(filelocation);
		ArrayList<EnvsAndSuites> suites = getSuitesToExecute(filelocation);
		runner.getRunnerparams().put(ExcelConstants.envdetails, env);
		runner.getRunnerparams().put(ExcelConstants.suitesdetails, suites);
		return runner;

	}

	public ArrayList<EnvsAndSuites> getEnvironmentDetails(String filelocation) throws Exception {
		GetRunnerDetails runnerenv = new GetRunnerDetails();
		return runnerenv.readDetails(filelocation, ExcelConstants.envdetails);

	}

	public ArrayList<EnvsAndSuites> getSuitesToExecute(String filelocation) throws Exception {
		GetRunnerDetails runnersuites = new GetRunnerDetails();
		return runnersuites.readDetails(filelocation, ExcelConstants.suitesdetails);

	}
}
