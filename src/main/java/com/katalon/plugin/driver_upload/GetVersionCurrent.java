package com.katalon.plugin.driver_upload;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public class GetVersionCurrent {
    public static String OSDetector () {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "Windows";
        } else if (os.contains("nux") || os.contains("nix")) {
            return "Linux";
        }else if (os.contains("mac")) {
            return "Mac";
        }else if (os.contains("sunos")) {
            return "Solaris";
        }else {
            return "Other";
        }
    }

    public static void driver(){
        RemoteWebDriver webDriver;
        Capabilities caps;

        String pathGeckoDriver = "D:\\Katalon\\katalon-studio-drivers-updater-plugin\\firefox_win64\\geckodriver.exe";
        String pathChromeDriver = "D:\\Katalon\\katalon-studio-drivers-updater-plugin - Copy\\chromedriver_win32\\chromedriver.exe";

        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, pathChromeDriver);
        caps = DesiredCapabilities.chrome();

        webDriver = new ChromeDriver(caps);

        caps = webDriver.getCapabilities();

        Map<String, String> a = (Map<String, String>) caps.getCapability("chrome");
        System.out.println(String.format("Driver Version: %s", a.get("chromedriverVersion")));
    }

    public static void readCurrentVersion() throws IOException {
        String storeCurrent = ".//version.json";
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Map<String,ObjectTemp>> listVersion = objectMapper.readValue(new File(storeCurrent), new TypeReference<Map<String, Map<String,ObjectTemp>>>(){});

        System.out.println(listVersion.get("chromedriver").get("win64").getVersion());
        System.out.println(listVersion.get("geckodriver").get("win64").getVersion());
        System.out.println(listVersion.get("iedriver").get("win64").getVersion());
        System.out.println(listVersion.get("seleniumDriver").get(".").getVersion());
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

//    public static String getOS(){
//        String nameOS = System.getProperty("os.name");
//        if (nameOS.contains("windows")) {
//            if (System.getProperty("os.arch").contains("64")){
//                return "win64";
//            } else{
//                return "win32";
//            }
//        } else if (nameOS.contains("linux")
//                || nameOS.contains("mpe/ix")
//                || nameOS.contains("freebsd")
//                || nameOS.contains("irix")
//                || nameOS.contains("digital unix")
//                || nameOS.contains("unix")) {
//            if (System.getProperty("os.arch").contains("64")){
//                return "win64";
//            } else{
//                return "win32";
//            }
//        } else if (nameOS.contains("mac os")) {
//            return "macosx";
//        }
//        return "";
//    }


    public static int compareInt(int n1, int n2){
        if (n1 > n2){
            return 1;
        } else if (n1 < n2){
            return 2;
        }
        return 0;
    }

    public static int compareVerison(String v1, String v2){
        int index1 = v1.indexOf(".");
        int index2 = v2.indexOf(".");
        if (index1 == -1 && index2 != -1) {
            return compareInt(Integer.parseInt(v1),
                    Integer.parseInt(v2.substring(0, index2)));
        }
        if (index2 == -1 && index1 != -1) {
            return compareInt(Integer.parseInt(v1.substring(0, index1)),
                    Integer.parseInt(v2));
        }
        if (index1 == -1 && index2 == -1) {
            return compareInt(Integer.parseInt(v1),
                    Integer.parseInt(v2));
        }
        int ver1 = Integer.parseInt(v1.substring(0, index1));
        int ver2 = Integer.parseInt(v2.substring(0, index2));
        if (ver1 == ver2) {
            return compareVerison(v1.substring(index1 + 1), v2.substring(index2 + 1));
        }
        if (ver1 > ver2) {
            return 1;
        } else {
            return 2;
        }

    }
    public static void main(String[] args) throws MalformedURLException {
////        driver();
//        try {
//            readCurrentVersion();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(compareVerison("2.43.60002", "2.43.60002"));
////        try {
////            Driver chromeDriver = Test.getChromeDrive("win64");
////            List<Driver> re = Test.getIEDriverSeleniumDirver("win64");
////
////            System.out.println(chromeDriver.toString());
////
////            System.out.println(re.get(0).toString());
////            System.out.println(re.get(1).toString());
////
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
        System.out.println(getOS());

    }
}