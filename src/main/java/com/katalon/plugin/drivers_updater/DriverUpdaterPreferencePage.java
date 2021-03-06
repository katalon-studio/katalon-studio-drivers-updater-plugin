package com.katalon.plugin.drivers_updater;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.swt.SWT.*;

public class DriverUpdaterPreferencePage extends PreferencePage {
    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        container.setLayout(new GridLayout(1, false));

        TableViewer tableViewer = new TableViewer(container);

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

//        String[] titles = {"Driver", "Current", "Latest", "Action"};
        String[] titles = {"Driver", "Current", "Latest"};
        
        TableColumn column;
        for (String title : titles) {
            column = new TableColumn(table, NULL);
            column.setText(title);
            column.setWidth(400);
        }
        try {          
        	GetLatestVersion get = new GetLatestVersion();
        	String dataCurrentVersion = get.getDataVersionCurrent();
        	
        	Map<String, Map<String,Driver>> listVersion =  GetLatestVersion.readCurrentVersion(dataCurrentVersion);

            String os = GetLatestVersion.getOS();

            final TableItem item1 = new TableItem(table, SWT.NONE);
            Driver chromeDriver = GetLatestVersion.getChromeDrive(os);
            String versionChromeLatest = chromeDriver.getVersion();
            item1.setText(new String[] {"Chrome",
                    listVersion.get("chromedriver").get(os).getVersion(),
                    versionChromeLatest});
            item1.setData("url", chromeDriver.getUrl());
            
            Driver geckoDriver = GetLatestVersion.getGeckoDriver(os);
            String versionGeckoDriverLatest = geckoDriver.getVersion();
            final TableItem item2 = new TableItem(table, SWT.NONE);
            item2.setText(new String[] {"Gecko",
                    listVersion.get("geckodriver").get(os).getVersion(),
                    versionGeckoDriverLatest});
            item2.setData("url", geckoDriver.getUrl());

            List<Driver> re = GetLatestVersion.getIEDriverSeleniumDriver(os);
            String versionIEDriverLatest = re.get(0).getVersion();
            String versionSeleniumDriver = re.get(1).getVersion();
            
            final TableItem item3 = new TableItem(table, SWT.NONE);
            item3.setText(new String[] {"IE",
                    listVersion.get("iedriver").get(os).getVersion(),
                    versionIEDriverLatest});
            item3.setData("url", re.get(0).getUrl());

            final TableItem item4 = new TableItem(table, SWT.NONE);
            item4.setText(new String[] {"Selenium",
                    listVersion.get("seleniumDriver").get(".").getVersion(),
                    versionSeleniumDriver});
            item4.setData("url", re.get(1).getUrl());
            
            List<TableItem> item = new ArrayList<>();
            item.add(item1);
            item.add(item2);
            item.add(item3);
            item.add(item4);
            
            if (os.contains("win")){
            	final TableItem item5 = new TableItem(table, SWT.NONE);
                Driver edgeDriver = GetLatestVersion.getEdgeDriver(os);
                String versionEdgeLatest = edgeDriver.getVersion();
                item5.setText(new String[] {"Edge",
                        listVersion.get("edgedriver").get(os).getVersion(),
                        versionEdgeLatest});
                item5.setData("url", edgeDriver.getUrl());
            }
            
            //For download browsers driver, don't use
//            TableEditor[] tableEditor = new TableEditor[4];
//
//            Button[] btnDownloads = new Button[4];
//
//            for (int i = 0; i < 4; i++) {
//              tableEditor[i] = new TableEditor(table);
//              btnDownloads[i] = new Button(table, SWT.PUSH);
//
//              btnDownloads[i].setText("Download");
//              btnDownloads[i].computeSize(SWT.DEFAULT, table.getItemHeight());
//              btnDownloads[i].addSelectionListener(new SelectionAdapter() {
//            	  @Override
//            	public void widgetSelected(SelectionEvent e) {
//            		  int idx = (int)e.widget.getData("index");
//	            		item.get(idx).setText(0, "new");
	            		
//	            		System.out.println(item.get(idx).getText(1).toString());
//	        			try {
//	        				String url = item.get(idx).getData("url").toString();
//	        				
//	        				System.out.println(url);
	        				
//		            		DownloadTask downloadTask = new DownloadTask(url);
//		                	File dir = new File(".\\");
//							System.out.println(downloadTask.downloadAndExtract(dir));
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//
//            	}
//              });
              
//              btnDownloads[i].setData("index", i);
//              
//              tableEditor[i].grabHorizontal = true;
//              tableEditor[i].minimumHeight = btnDownloads[i].getSize().y;
//              tableEditor[i].minimumWidth = btnDownloads[i].getSize().x;
//
//              tableEditor[i].setEditor(btnDownloads[i], item.get(i), 3);
//            }
            for (int i = 0, n = table.getColumnCount(); i < n; i++) {
                table.getColumn(i).pack();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return container;
    }
}
