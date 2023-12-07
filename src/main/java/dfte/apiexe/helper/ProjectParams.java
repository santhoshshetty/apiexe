package dfte.apiexe.helper;

public class ProjectParams {

	public static String execFilePath;
	public static String testDataLocation;
	public static String reportsLocation;
	public static String buildNumber;
	public static String projectInExecution;
	public static String envrinonmentURL;

	public void setProjectParams(String filePath, String td, String rep, String bno, String proj, String env) {
		execFilePath = filePath;
		testDataLocation = td;
		reportsLocation = rep;
		buildNumber = bno;
		projectInExecution = proj;
		envrinonmentURL = env;
	}
}
