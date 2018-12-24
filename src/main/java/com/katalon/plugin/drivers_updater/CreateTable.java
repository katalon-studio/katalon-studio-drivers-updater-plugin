package com.katalon.plugin.drivers_updater;

import static org.eclipse.swt.SWT.NULL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateTable {
	
	private static Map<String, Map<String,ObjectTemp>> listVersion;
	
    public static void readCurrentVersion() throws IOException {
        String storeCurrent = "D:\\Katalon\\katalon-studio-drivers-updater-plugin\\version.json";
        ObjectMapper objectMapper = new ObjectMapper();

        listVersion = objectMapper.readValue(new File(storeCurrent), new TypeReference<Map<String, Map<String,ObjectTemp>>>(){});
    }

	
	  public static void main(String[] args) {
	        Display display = new Display();
	        Shell shell = new Shell(display);

	        shell.setLayout(new FillLayout());
	        
	        TableViewer tableViewer = new TableViewer(shell);
	        tableViewer.getTable().setHeaderVisible(true);
	        tableViewer.getTable().setLinesVisible(true);
	        tableViewer.setContentProvider(new ArrayContentProvider());
	        
	        Table table = tableViewer.getTable();
	        
	        String[] titles = {"Driver", "Current", "Lastest", "Action"};
	        
	        TableColumn column;
	        for (String title : titles) {
	            column = new TableColumn(table, NULL);
	            column.setText(title);
	            column.setWidth(400);
	        }

	        try {          
	            readCurrentVersion();
	            System.out.println("read current version done!");

	            String os = "win64";

	            System.out.println(os);

	            List<TableItem> item = new ArrayList<>();
	            
	            TableItem item1 = new TableItem(table, SWT.NONE);
	            Driver chromeDriver = GetLastestVersion.getChromeDrive(os);
	            String versionChromeLastest = chromeDriver.getVersion();
	            item1.setText(new String[] {"Chrome",
	                    listVersion.get("chromedriver").get(os).getVersion(),
	                    versionChromeLastest});
	            item1.setData("url", chromeDriver.getUrl());
	            	            
	            Driver geckoDriver = GetLastestVersion.getGeckoDriver(os);
	            String versionGeckoDriverLastest = geckoDriver.getVersion();
	            final TableItem item2 = new TableItem(table, SWT.NONE);
	            item2.setText(new String[] {"Gecko",
	                    listVersion.get("geckodriver").get(os).getVersion(),
	                    versionGeckoDriverLastest});
	            item2.setData("url", geckoDriver.getUrl());
	            
	            List<Driver> re = GetLastestVersion.getIEDriverSeleniumDriver(os);
	            String versionIEDriverLastest = re.get(0).getVersion();
	            String versionSeleniumDriver = re.get(1).getVersion();
	            
	            final TableItem item3 = new TableItem(table, SWT.NONE);
	            item3.setText(new String[] {"IE",
	                    listVersion.get("iedriver").get(os).getVersion(),
	                    versionIEDriverLastest});
	            item3.setData("url", re.get(0).getUrl());
	            
	            final TableItem item4 = new TableItem(table, SWT.NONE);
	            item4.setText(new String[] {"Selenium",
	                    listVersion.get("seleniumDriver").get(".").getVersion(),
	                    versionSeleniumDriver});
	            item4.setData("url", re.get(1).getUrl());
	            
	            item.add(item1);
	            item.add(item2);
	            item.add(item3);
	            item.add(item4);
	            
	            TableEditor[] tableEditor = new TableEditor[4];

	            Button[] btnDownloads = new Button[4];

	            for (int i = 0; i < 4; i++) {
	              tableEditor[i] = new TableEditor(table);
	              btnDownloads[i] = new Button(table, SWT.PUSH);

	              btnDownloads[i].setText("Download");
	              btnDownloads[i].computeSize(SWT.DEFAULT, table.getItemHeight());
	              btnDownloads[i].addSelectionListener(new SelectionAdapter() {
	            	  @Override
	            	public void widgetSelected(SelectionEvent e) {
	            		int idx = (int)e.widget.getData("index");
//	            		item.get(idx).setText(0, "new");
	            		
//	            		System.out.println(item.get(idx).getText(1).toString());
	            		System.out.println(item.get(idx).getData("url"));
	            	}
	              });
	              btnDownloads[i].setData("index", i);

	              tableEditor[i].grabHorizontal = true;
	              tableEditor[i].minimumHeight = btnDownloads[i].getSize().y;
	              tableEditor[i].minimumWidth = btnDownloads[i].getSize().x;

	              tableEditor[i].setEditor(btnDownloads[i], item.get(i), 3);
	            }
	            
	            
	            for (int i = 0, n = table.getColumnCount(); i < n; i++) {
	                table.getColumn(i).pack();
	            }

	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        
	        shell.open();
	        while(!shell.isDisposed())
	        {

	            if(!display.readAndDispatch())
	            {
	                display.sleep();
	            }
	        }

	        display.dispose();
	  }
}
