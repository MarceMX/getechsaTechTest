package org.getecsa;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.getecsa.pages.FlightStatus;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.MDC;
import org.getecsa.pages.FlightStatus;
import org.getecsa.pages.HomePage;
import org.getecsa.utils.LocalUtils;

import java.time.Duration;
import java.util.Objects;

@Slf4j
public class AmericanAirlinesTests {
	
	WebDriver driver;
	static LocalUtils localUtils;
	final String LOG_BREAK ="\n\n##################################################################\n";
	
	
	public void configureWebDriver(){
//		String headless = "headless"; //Uncomment this line if you want to execute headless test
		String headless = ""; //Uncomment this line if you want to execute test with open browser
		driver = (Objects.nonNull(headless) && !headless.isEmpty())?
				new FirefoxDriver(new FirefoxOptions(){{addArguments("--headless");}}) : new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
		
		localUtils = new LocalUtils(driver);
		driver.get("about:blank");
	}
	
	@BeforeEach
	public void launchBrowser(TestInfo testInfo){
		String logID="::launchBrowser([testInfo]): ";
		configureWebDriver();
		localUtils.newLogFile(testInfo.getDisplayName());
		log.trace("{}Open WebBrowser", logID);
		driver.get("https://www.aa.com/homePage.do?locale=es_MX");
	}
	
	@AfterEach
	public void closeBrowser(TestInfo testInfo){
		String logID = "#closeApp():";
		log.trace("{} Closing Browser", logID);
		driver.quit();
		localUtils.closeLogFile();
	}
	
	@AfterAll
	public static void discardLogs(){
		String logID="::discardLogs([]): ";
		localUtils.discardLogs();
		log.info("{}finalized without exceptions", logID);
	}
	
	
	@SneakyThrows
	@Test
	@DisplayName("Wrong File Automated Functional Test")
	public void wrongFlightTest(TestInfo testInfo){
		String logID="::wrongFlightTest(testInfo): ";
		String testID=testInfo.getDisplayName();
		
		log.info("{}{}Start {}", LOG_BREAK, testID, LOG_BREAK);
		
		try{
			HomePage homePage = PageFactory.initElements(driver, HomePage.class);;
			FlightStatus flightStatus;
			
			homePage.navigateToFlightStatus();
			
			flightStatus = PageFactory.initElements(driver, FlightStatus.class);
			flightStatus.enterAndSearchFlightNumber("98675");
			
			//This line is set to validate that on error, the exception code works properly
			//if(((Integer)1).equals(1)) throw new RuntimeException("Forced exception");
			
			String errorMsg = driver.findElement(By.id("flightNumber.errors")).getText();
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(errorMsg)
					.as("Correct error message is displayed")
					.contains("Vuelo no encontrado. Intente con otro número o haga la búsqueda por ciudades.");
			softly.assertAll();
			localUtils.takeScreenshot(localUtils.getScreenshotFileName(testID));
			
		}catch(Exception ex){
			log.error("{}An Error has occurred. Generating evidences...", logID, ex);
			localUtils.takeScreenshot(localUtils.getScreenshotErrorFileName(testID));
			log.trace("{}Backing logs up", logID);
			localUtils.backupLogs(testID, "errors");
			throw new RuntimeException( String.format( "An Exception has occurred when executing %s", testID), ex);
		}
		log.info("{}Finish", testID);
	}
	
	
	
	@SneakyThrows
	@Test
	@DisplayName("No Flight Number Automated Functional Test")
	public void noFlightNumberProvided(TestInfo testInfo){
		String logID="::noFlightNumberProvided(): ";
		log.trace("{}Start ", logID);
		String testID=testInfo.getDisplayName();
		MDC.put("id",testID);

		log.info("{}{} Start{}", LOG_BREAK, testID, LOG_BREAK);
		try {
			
			HomePage homePage;
			FlightStatus flightStatus;
			
			homePage = PageFactory.initElements(driver, HomePage.class);
			homePage.navigateToFlightStatus();
			
			flightStatus = PageFactory.initElements(driver, FlightStatus.class);
			flightStatus.enterAndSearchFlightNumber("");
			
			String errorMsg = driver.findElement(By.id("flightNumber.errors")).getText();
			SoftAssertions softly = new SoftAssertions();
			softly.assertThat(errorMsg)
					.as("Correct error message is displayed")
					.contains("Debe ingresar el número del vuelo.");
			softly.assertAll();
			localUtils.takeScreenshot(localUtils.getScreenshotFileName(testID));
			
		}catch(Exception ex){
			log.error("{}An Error has occurred. Generating evidences...", logID, ex);
			localUtils.takeScreenshot(localUtils.getScreenshotErrorFileName(testID));
			log.trace("{}Backing logs up", logID);
			localUtils.backupLogs(testID, "errors");
			throw new RuntimeException( String.format( "An Exception has occurred when executing %s", testID), ex);
		}
		log.info("{}Finish", logID);
	}
	
}

