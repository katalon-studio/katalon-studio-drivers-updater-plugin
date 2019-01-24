## katalon-studio-drivers-updater-plugin
Drivers Updater plugin is an advanced plugin that supports users to check for update the latest version Selenium drivers(chromedriver, gecko, IEDriver, edge), Appium, and other mobile drivers.

#### How to use katalon-studio-drivers-updater-plugin
In **Katalon Studio**, 


#### Build
Requirements:
- JDK 1.8
- Maven 3.3+

Build

`mvn clean install`

#### How to test in Katalon Studio
- Checkout or get a build of branch `katalon staging-plugin` of KS.
- After KS opens, please click on `Plugin` menu, select `Install Plugin` and choose the generated jar file.
- If you want to reload this plugin, please click on `Plugin` menu, select `Uninstall Plugin` then select `Install Plugin` again. 

#### To see Plugin
- After install Plugin to KS, please click on `Window` menu.
- Select `Katalon Studio Preferences`, then select `Katalon` and click on `Plugins`.

#### Main function
- Check the latest version Selenium drivers (include chromedriver, gecko, IEDriver, edge).
- Show the current version and latest version in `Katalon Studio Preferences/Katalon/Plugins`.
- Show the log in the end of execution log in case the driver is not the latest one, include the current version, lastes version, url download and store directory.

#### Future function
- Download Selenium drivers from url.