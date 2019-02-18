## katalon-studio-drivers-updater-plugin
The Drivers Updater plugin is an advanced plugin that allows users to check for the latest versions of Selenium drivers (chromedriver, gecko, IEDriver, edge), Appium, and other mobile drivers.

#### How to use katalon-studio-drivers-updater-plugin
In **Katalon Studio** 


#### Build
Requirements:
- JDK 1.8
- Maven 3.3+

Build

`mvn clean install`

#### How to test in Katalon Studio
- Check out or get a build of branch `katalon staging-plugin` of Katalon Studio
- After Katalon Studio opens, click on the `Plugin` menu, select `Install Plugin`, and choose the generated JAR file.
- If you want to reload this plugin, click on the `Plugin` menu, select `Uninstall Plugin` then select `Install Plugin` again. 

#### View Plugin
- After installing Drivers Updater Plugin to Katalon Studio, click on the `Window` menu.
- Select `Katalon Studio Preferences`, then select `Katalon`, and click on `Plugins`.

#### Main function
- Check the latest version of Selenium drivers (including chromedriver, gecko, IEDriver, edge).
- Show the current and latest versions in `Katalon Studio Preferences/Katalon/Plugins`.
- Show the log at the end of the execution log if the driver is not the latest version. Include the current version, lastest version, url download, and store directory.

#### Future function
- Download Selenium drivers from URL.
