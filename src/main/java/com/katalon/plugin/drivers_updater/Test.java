package com.katalon.plugin.drivers_updater;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.github.*;

import javax.json.JsonObject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.URL;
import java.util.*;

import static javax.xml.stream.XMLInputFactory.IS_NAMESPACE_AWARE;

public class Test {
    private static String userName = "";
    private static String password = "";

    private static ListBucketResult readDataFromURL(String link) {
        try {
            URL url = new URL(link);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            StringBuilder out = new StringBuilder();
            while ((line = in.readLine()) != null) {
                out.append(line);
            }
            in.close();

            JAXBContext context = JAXBContext.newInstance(ListBucketResult.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(IS_NAMESPACE_AWARE, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StringBufferInputStream(out.toString()));

            xsr = xif.createFilteredReader(xsr, reader -> {
                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                    return reader.getText().trim().length() > 0;
                }
                return true;
            });

            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ListBucketResult) unmarshaller.unmarshal(xsr);
        } catch (IOException | JAXBException | XMLStreamException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void getChromeDrive() throws IOException {
        String url = "http://chromedriver.storage.googleapis.com/";
        ListBucketResult data = readDataFromURL(url);
        List<Driver> drivers = new ArrayList<>();

        for (Contents i : data.getContents()) {
            String text = i.getKey();

            int index = text.indexOf("/");
            if (index == -1) {
                continue;
            }

            String version = text.substring(0, index);
            String name = text.substring(index + 1);
            String urlDrive = url + text;
            String os = "";

            if (text.contains("mac32")) {
                continue;
            } else if (text.contains("mac64")) {
                os = "mac";
            } else if (text.contains("linux32")) {
                os = "linux32";
            } else if (text.contains("linux64")) {
                os = "linux64";
            } else if (text.contains("win")) {
                os = "windowns";
            } else {
                continue;
            }
            Driver temp = new Driver();
            temp.setName(name);
            temp.setOs(os);
            temp.setUrl(urlDrive);
            temp.setVersion(version);
            drivers.add(temp);
        }
        writeToJson(drivers, "chromeDriver.json");
    }

    private static void getIEDriverSeleniumDirver() throws IOException {
        String url = "https://selenium-release.storage.googleapis.com/";
        ListBucketResult data = readDataFromURL(url);
        List<Driver> driversIE = new ArrayList<>();
        List<Driver> driversSelenium = new ArrayList<>();
        for (Contents i : data.getContents()) {
            String text = i.getKey();

            int index = text.indexOf("/");
            if (index == -1) {
                continue;
            }

            String version = text.substring(0, index);
            String name = text.substring(index + 1);
            String urlDrive = url + text;
            String os = "";

            if (text.contains("IEDriverServer")) {
                if (text.contains("Win32") || text.contains("win32")) {
                    os = "win32";
                } else if (text.contains("x64")) {
                    os = "win64";
                } else {
                    continue;
                }
                Driver temp = new Driver();
                temp.setName(name);
                temp.setOs(os);
                temp.setUrl(urlDrive);
                temp.setVersion(version);
                driversIE.add(temp);
            } else if (text.contains("selenium-server-standalone")) {
                Driver temp = new Driver();
                temp.setName(name);
                temp.setOs(os);
                temp.setUrl(urlDrive);
                temp.setVersion(version);
                driversSelenium.add(temp);
            }
        }

        writeToJson(driversIE, "ieDriver.json");
        writeToJson(driversSelenium, "seleniumDriver.json");
    }

    private static void getGeckoDriver() throws IOException {
        List<Driver> driversGecko = new ArrayList<>();
        Github github = new RtGithub(userName, password);
        Coordinates coords = new Coordinates.Simple("mozilla/geckoDriver");

        Repo repo = github.repos().get(coords);
        for (Release release : repo.releases().iterate()) {
            for (ReleaseAsset k : release.assets().iterate()) {

                JsonObject object = k.json();
                String name = object.getString("name");
                String url = object.getString("browser_download_url");
                String os = "";

                String version = url.substring(url.lastIndexOf("download/") + 9, url.lastIndexOf("/"));
                if (version.contains("v")) {
                    version = version.replace("v", "");
                }

                if (name.contains("mac32")) {
                    continue;
                } else if (name.contains("macos") || name.contains("osx") || name.contains("OSX")) {
                    os = "mac";
                } else if (name.contains("linux32")) {
                    os = "linux32";
                } else if (name.contains("linux64")) {
                    os = "linux64";
                } else if (name.contains("win32")) {
                    os = "win32";
                } else if (name.contains("win64")) {
                    os = "win64";
                } else if(name.contains("win.") || name.contains("windows")){
                    os = "windows";
                }
                else {
                    continue;
                }

                Driver temp = new Driver();
                temp.setName(name);
                temp.setOs(os);
                temp.setUrl(url);
                temp.setVersion(version);
                driversGecko.add(temp);
            }
        }
        writeToJson(driversGecko, "geckoDriver.json");
    }

    private static void writeToJson(List<Driver> drivers, String fileJson) throws IOException {
        Map<String, List<Driver>> dictionary = new HashMap<>();
        for (Driver i : drivers) {
            String version = i.getVersion();
            if (Integer.parseInt(version.substring(0, version.indexOf("."))) > 10) {
                continue;
            }
            if (dictionary.get(version) == null) {
                List<Driver> temp = new ArrayList<>();
                temp.add(i);
                dictionary.put(version, temp);
            } else {
                List<Driver> temp = dictionary.get(version);
                temp.add(i);
                dictionary.put(version, temp);
            }
        }
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File(fileJson), dictionary);
    }

    public static void main(String[] args) {
        try {
            getChromeDrive();
            getIEDriverSeleniumDirver();
            getGeckoDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
