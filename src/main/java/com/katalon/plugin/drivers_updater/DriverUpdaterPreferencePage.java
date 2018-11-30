package com.katalon.plugin.drivers_updater;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class DriverUpdaterPreferencePage extends PreferencePage {

    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        
        //TODO: Add driver updater layout here

        return container;
    }
}
