package com.katalon.plugin.driver_upload;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.Map;
import java.util.List;

import static org.eclipse.swt.SWT.*;

public class DriverUpdaterPreferencePage extends PreferencePage {

    private static Map<String, Map<String,ObjectTemp>> listVersion;

    public static void readCurrentVersion() throws IOException {
        String storeCurrent = "D:\\Katalon\\katalon-studio-drivers-updater-plugin\\version.json";
        ObjectMapper objectMapper = new ObjectMapper();

        listVersion = objectMapper.readValue(new File(storeCurrent), new TypeReference<Map<String, Map<String,ObjectTemp>>>(){});
    }

    public static String getOS(){
        String nameOS = System.getProperty("os.name");

        if (nameOS.contains("Windows")) {
            if (System.getProperty("os.arch").contains("64")){
                return "win64";
            } else{
                return "win32";
            }
        } else if (nameOS.contains("linux")
                || nameOS.contains("mpe/ix")
                || nameOS.contains("freebsd")
                || nameOS.contains("irix")
                || nameOS.contains("digital unix")
                || nameOS.contains("unix")) {
            if (System.getProperty("os.arch").contains("64")){
                return "win64";
            } else{
                return "win32";
            }
        } else if (nameOS.contains("mac os")) {
            return "macosx";
        }
        return "";
    }

    
    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        
        //TODO: Add driver updater layout here
        container.setLayout(new GridLayout(1, false));

        TableViewer tableViewer = new TableViewer(container);

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        String[] titles = {"Driver", "Current", "Lastest"};

        for (String title : titles) {
            TableColumn column = new TableColumn(table, NULL);
            column.setText(title);
            column.setWidth(400);
        }

        try {          
            readCurrentVersion();
            System.out.println("read current version done!");

            String os = getOS();

            System.out.println(os);

            final TableItem item1 = new TableItem(table, SWT.NONE);
            Driver chromeDriver = GetLastestVersion.getChromeDrive(os);
            String versionChromeLastest = chromeDriver.getVersion();
            item1.setText(new String[] {"Chrome",
                    listVersion.get("chromedriver").get(os).getVersion(),
                    versionChromeLastest});

            Driver GeckoDriver = GetLastestVersion.getGeckoDriver(os);
            String versionGeckoDriverLastest = GeckoDriver.getVersion();
            final TableItem item2 = new TableItem(table, SWT.NONE);
            item2.setText(new String[] {"Gecko",
                    listVersion.get("geckodriver").get(os).getVersion(),
                    versionGeckoDriverLastest});

            List<Driver> re = GetLastestVersion.getIEDriverSeleniumDriver(os);
            String versionIEDriverLastest = re.get(0).getVersion();
            String versionSeleniumDriver = re.get(1).getVersion();
            
            final TableItem item3 = new TableItem(table, SWT.NONE);
            item3.setText(new String[] {"IE",
                    listVersion.get("iedriver").get(os).getVersion(),
                    versionIEDriverLastest});

            final TableItem item4 = new TableItem(table, SWT.NONE);
            
            item4.setText(new String[] {"Selenium",
                    listVersion.get("seleniumDriver").get(".").getVersion(),
                    versionSeleniumDriver});

            for (int i = 0, n = table.getColumnCount(); i < n; i++) {
                table.getColumn(i).pack();
            }
            System.out.println("Display done");

        } catch (IOException e) {
            e.printStackTrace();
        }


        return container;
    }
}
