package com.katalon.plugin.driver_upload;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

public class PlatformUtils {
	public static File getConfigurationFolder() throws IOException {
        return new File(FileLocator.resolve(Platform.getConfigurationLocation().getURL()).getFile());
    }
}
