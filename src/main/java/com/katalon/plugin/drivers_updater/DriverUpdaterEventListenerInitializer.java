package com.katalon.plugin.drivers_updater;

import org.osgi.service.event.Event;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.event.ExecutionEvent;
import com.katalon.platform.api.extension.EventListenerInitializer;

public class DriverUpdaterEventListenerInitializer implements EventListenerInitializer {

    public void registerListener(EventListener eventListener) {
    	eventListener.on(Event.class, event -> {
    		try {
                if (ExecutionEvent.TEST_SUITE_FINISHED_EVENT.equals(event.getTopic())) {
//    				System.out.println("Check for update browser drivers.");
    				GetLatestVersion getVersion = new GetLatestVersion();
    				
    		    	String os = GetLatestVersion.getOS();  
    		    	String data = getVersion.getDataVersionCurrent();
    		    	
    				Map<String, Map<String,Driver>> current = GetLatestVersion.readCurrentVersion(data);
    				Driver chromeDriver;
    				chromeDriver = GetLatestVersion.getChromeDrive(os);
    				String currentVersionChromeDriver = current.get("chromedriver").get(os).getVersion();
    				
    				if (GetLatestVersion.compareVerison(currentVersionChromeDriver, chromeDriver.getVersion())  == 2){
    					System.out.printf("A new version of chrome driver, current version: %s, latest version %s.\n", currentVersionChromeDriver, chromeDriver.getVersion());
    				}
    				Driver getGeckoDriver = GetLatestVersion.getGeckoDriver(os);
    				
    				String currentVersionGeckoDriver = current.get("geckodriver").get(os).getVersion();
    				if (GetLatestVersion.compareVerison(currentVersionGeckoDriver, getGeckoDriver.getVersion())  == 2){
    					System.out.printf("A new version of gecko driver, current version: %s, latest version %s.\n", currentVersionGeckoDriver, getGeckoDriver.getVersion());
    				}
    				
    				List<Driver> re = GetLatestVersion.getIEDriverSeleniumDriver(os);
    				String currentVersionIEDriver = current.get("iedriver").get(os).getVersion();
    				if (GetLatestVersion.compareVerison(currentVersionIEDriver, re.get(0).getVersion())  == 2){
    					System.out.printf("A new version of IE driver, current version: %s, latest version %s.\n", currentVersionIEDriver, re.get(0).getVersion());
    				}
    				
    				String currentVersionSeleniumDriver = current.get("seleniumDriver").get(".").getVersion();
    				if (GetLatestVersion.compareVerison(currentVersionSeleniumDriver, re.get(1).getVersion())  == 2){
    					System.out.printf("A new version of selenium driver, current version: %s, latest version %s.\n", currentVersionSeleniumDriver, re.get(1).getVersion());
    				}
    				
    				if (os.contains("win")){
    					Driver edgeDriver = GetLatestVersion.getEdgeDriver(os);
    					
    					String currentVersionEdgeDriver = current.get("edgedriver").get(os).getVersion();

        				if (GetLatestVersion.compareVerison(currentVersionEdgeDriver, edgeDriver.getVersion())  == 2){
        					System.out.printf("A new version of edge driver, current version: %s, latest version %s.\n", currentVersionEdgeDriver, edgeDriver.getVersion());
        				}
    				}
    			}
				
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
    	});
    }
}
