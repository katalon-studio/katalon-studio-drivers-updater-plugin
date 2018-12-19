package com.katalon.plugin.driver_upload;

//import java.awt.Event;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.katalon.platform.api.extension.event.EventListener;
import com.katalon.platform.api.extension.event.EventListenerInitializer;
import com.katalon.platform.api.extension.execution.ExecutionEvent;

import org.osgi.service.event.Event;
//import org.osgi.service.prefs.Preferences;

public class DriverUpdaterEventListenerInitializer implements EventListenerInitializer {

    public void registerListener(EventListener eventListener) {
    	eventListener.on(Event.class, event -> {
    		try {
    			if (ExecutionEvent.TEST_SUITE_FINISHED_EVENT.equals(event.getTopic())){
    				System.out.println("Check for update brower driver.");
    				GetLastestVersion getVersion = new GetLastestVersion();
    				
    		    	String os = GetLastestVersion.getOS();  
    		    	String data = getVersion.getDataVersionCurrent();
    		    	
    				Map<String, Map<String,ObjectTemp>> current = GetLastestVersion.readCurrentVersion(data);
    				Driver chromeDriver;
    				chromeDriver = GetLastestVersion.getChromeDrive(os);
    				String currentVersionChromeDriver = current.get("chromedriver").get(os).getVersion();
    				
    				if (GetLastestVersion.compareVerison(currentVersionChromeDriver, chromeDriver.getVersion())  == 2){
    					System.out.printf("A new version of chrome driver, current version: %s, lastest version %s.\n", currentVersionChromeDriver, chromeDriver.getVersion());
    				}
    				Driver getGeckoDriver = GetLastestVersion.getGeckoDriver(os);
    				
    				String currentVersionGeckoDriver = current.get("geckodriver").get(os).getVersion();
    				if (GetLastestVersion.compareVerison(currentVersionGeckoDriver, getGeckoDriver.getVersion())  == 2){
    					System.out.printf("A new version of gecko driver, current version: %s, lastest version %s.\n", currentVersionGeckoDriver, getGeckoDriver.getVersion());
    				}
    				
    				List<Driver> re = GetLastestVersion.getIEDriverSeleniumDriver(os);
    				String currentVersionIEDriver = current.get("iedriver").get(os).getVersion();
    				if (GetLastestVersion.compareVerison(currentVersionIEDriver, re.get(0).getVersion())  == 2){
    					System.out.printf("A new version of IE driver, current version: %s, lastest version %s.\n", currentVersionIEDriver, re.get(0).getVersion());
    				}
    				
    				String currentVersionSeleniumDriver = current.get("seleniumDriver").get(".").getVersion();
    				if (GetLastestVersion.compareVerison(currentVersionSeleniumDriver, re.get(1).getVersion())  == 2){
    					System.out.printf("A new version of selenium driver, current version: %s, lastest version %s.\n", currentVersionSeleniumDriver, re.get(1).getVersion());
    				}
    			}
				
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
    	});
    }
}
