package org.getecsa.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import javax.swing.text.Utilities;

@Slf4j
public class FlightStatus extends Utilities {
	@FindBy(how = How.XPATH,using = "//span[contains(text(),'Número de vuelo')]")
	WebElement flightNumberLnk;
	@FindBy(id = "flightNumber")
	WebElement flightNumberTxt;
	@FindBy(id = "flightSchedulesSearchButton")
	WebElement searchFlightBtn;
	
	public void enterAndSearchFlightNumber(String flightNumber){
		String logID = "#enterAndSearchFlightNumber():";
		log.trace("{} Start", logID);
		flightNumberLnk.click();
		flightNumberTxt.sendKeys(flightNumber);
		searchFlightBtn.click();
		log.trace("{} Searching flight:", logID);
	}
}