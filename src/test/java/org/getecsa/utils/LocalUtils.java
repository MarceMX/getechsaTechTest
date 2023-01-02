package org.getecsa.utils;

import ch.qos.logback.classic.ClassicConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.MDC;

import java.io.File;
import java.time.LocalDate;

@Slf4j
public class LocalUtils {
	final WebDriver driver;
	final static String PROJECT_BUILD_DIRECTORY = "./target";
	public LocalUtils(final WebDriver driver) {
		this.driver = driver;
	}
	
	@SneakyThrows
	public void takeScreenshot(String screenShotFileName){
		String logID="::takeScreenshot([screenShotFileName]): ";
		log.trace("{}Start ", logID);
		try {
			FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), new File(screenShotFileName));
		}catch(Exception ex){
			log.trace("{} Not able to take a Screenshot this assumes HtmlUnitDriver is being used and proceeds to try to take the screenshot to this driver. ", logID);
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy somewhere
			FileUtils.copyFile(scrFile, new File(screenShotFileName));
		}
	}
	
	public String getScreenshotFileName(final String testID) {
		return "./target/test-reports/screenshots/" + testID + " " + LocalDate.now().toString().replace(":| ", "_") + ".png";
	}
	
	public String getScreenshotErrorFileName(final String testCaseId) {
		return "./target/test-reports/errors/"+testCaseId+"/" + testCaseId + " " + LocalDate.now().toString().replace(":| ", "_") + "-ERROR.png";
	}
	
	public void newLogFile(String testID){
		MDC.put("id", testID);
	}
	
	@SneakyThrows
	public void discardLogFile(String testID) {
		File file = new File(String.format("%s/test-reports/logs/%s.log", PROJECT_BUILD_DIRECTORY, testID));
		FileUtils.delete(file);
	}
	
	@SneakyThrows
	public void discardLogs() {
		File logsDirectory = new File(String.format("%s/test-reports/logs", PROJECT_BUILD_DIRECTORY));
		FileUtils.forceDeleteOnExit(logsDirectory);
	}
	
	@SneakyThrows
	public void backupLogs(String testID, String folderID) {
		FileUtils.copyFile(new File( String.format("%s/test-reports/logs/%s.log", PROJECT_BUILD_DIRECTORY, testID)),
						   new File( String.format("%s/test-reports/%s/%s/%s.log", PROJECT_BUILD_DIRECTORY, folderID, testID, testID) ));
	}
	
	public void closeLogFile() {
		log.info(ClassicConstants.FINALIZE_SESSION_MARKER, "Finalize Session");
	}
	
}
