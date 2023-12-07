package dfte.apiexe.controller;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dfte.apiexe.helper.EmailObj;
import dfte.apiexe.model.Exec;
import dfte.apiexe.model.ExecLogRecords;
import dfte.apiexe.model.RunnerParams;
import dfte.apiexe.service.EmailService;
import dfte.apiexe.service.RunAPIService;

@RestController
@RequestMapping("/apiexe")
public class ExecController {

	@Autowired
	RunAPIService runAPIService;

	@Autowired
	EmailService emailService;

	@PostMapping("/runAPI")
	public void runAPI(@RequestBody Exec exec) throws Exception {
		runAPIService.runSuites(exec);
	}

	@GetMapping("/readAPILog")
	public ExecLogRecords readExecutionLog() {
		return runAPIService.returnExecLog();
	}

	@GetMapping("/readRunnerParams")
	public RunnerParams getEnvironmentDetails(@RequestParam String filelocation) throws Exception {
		String location = java.net.URLDecoder.decode(filelocation, StandardCharsets.UTF_8.name());
		return runAPIService.getRunnerDetails(location);
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Object> downloadFile(String filePath) throws Exception {
		String filename = "C:\\Users\\skumargoureshetty\\Documents\\EGC\\src\\debug.log";
		File file = new File(filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/text")).body(resource);
		return responseEntity;

	}

	@GetMapping("/viewReport")
	public ResponseEntity<Object> viewHTMLReport(String filePath) throws Exception {
		String filename = "C:\\Users\\skumargoureshetty\\Documents\\GE\\Results_APIExec\\APIExec\\Results\\extentReport.html";
		File file = new File(filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("text/html")).body(resource);
		return responseEntity;
	}

	@PostMapping("/sendMail")
	public void sendEmail(@RequestBody EmailObj emailObj) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("test", "emailservice");
		emailService.sendEmail(emailObj, model);
	}

}