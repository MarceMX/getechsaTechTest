package org.getecsa.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class HomePage {
    @FindBy(css="#travel-information-expander")
    WebElement travelInfoLnk;
    @FindBy(xpath="/html/body/header/div/div[2]/nav/ul/li[3]/div/ul[3]/li[1]/a")
    WebElement flightStatusLnk;
    
    public void navigateToFlightStatus() {
        String logID = "::navigateToFlightStatus():";
        log.trace("{} Start", logID);
        travelInfoLnk.click();
        flightStatusLnk.click();
        log.trace("{} Navigation: {}", logID, "Go to Flight Status screen");
    }
}