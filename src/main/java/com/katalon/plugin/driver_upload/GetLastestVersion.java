package com.katalon.plugin.driver_upload;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

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

public class GetLastestVersion {
//    private static final String USERNAME = "plugingettags";
//    private static final String PASSWORD = "123!23Qwe";

    private static int compareInt(int n1, int n2){
        if (n1 > n2){
            return 1;
        } else if (n1 < n2){
            return 2;
        }
        return 0;
    }

    private static int compareVerison(String v1, String v2){
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

            System.setProperty("javax.xml.bind.context.factory", JAXBContextFactory.class.getName());
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

    public static Driver getChromeDrive(String currentOS) throws IOException {
        System.out.println("In getting version chromeDriver");
        String os = "";
        if (currentOS.contains("win")){
            os = "win";
        } else{
            os = currentOS;
        }

        String url = "http://chromedriver.storage.googleapis.com/";
        ListBucketResult data = readDataFromURL(url);
//        List<Driver> drivers = new ArrayList<>();
        Driver result = null;

        assert data != null;

        for (Contents i : data.getContents()) {
            String text = i.getKey();

            int index = text.indexOf("/");
            if (index == -1) {
                continue;
            }

            String version = text.substring(0, index);
            if (version.contains("icons")) continue;

            if (Integer.parseInt(version.substring(0, version.indexOf("."))) > 10) {
                continue;
            }

            String name = text.substring(index + 1);
            String urlDrive = url + text;

            if (text.contains(os)){
                Driver temp = new Driver();
                temp.setName(name);
                temp.setUrl(urlDrive);
                temp.setVersion(version);

                temp.setOs(currentOS);
//                drivers.add(temp);

                if (result == null){
                    result = temp;
                } else{
                    if (compareVerison(result.getVersion(), temp.getVersion()) == 2){
                        result = temp;
                    }
                }
            }
        }

        System.out.println("Done!");

        return result;
    }

    public static List<Driver> getIEDriverSeleniumDriver(String currentOS) throws IOException {
        System.out.println("In getting version IE and Selenium Driver");
        String url = "https://selenium-release.storage.googleapis.com/";
        ListBucketResult data = readDataFromURL(url);
        List<Driver> driversIE = new ArrayList<>();
        List<Driver> driversSelenium = new ArrayList<>();

        Driver resultIE = null;
        Driver resultSelenium = null;

        for (Contents i : data.getContents()) {
            String text = i.getKey();

            int index = text.indexOf("/");
            if (index == -1) {
                continue;
            }

            String version = text.substring(0, index);

            if (version.contains("icons")) continue;

            if (Integer.parseInt(version.substring(0, version.indexOf("."))) > 10) continue;

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


                if(os.contains(currentOS)){
                    if (resultIE == null){
                        resultIE = temp;
                    } else{
                        String verRe = resultIE.getVersion();
                        String verTemp = temp.getVersion();

                        if (verRe.contains("-beta")){
                            verRe = verRe.replace("-beta", ".");
                        }
                        if (verTemp.contains("-beta")){
                            verTemp = verRe.replace("-beta", ".");
                        }

                        if (compareVerison(verRe, verTemp) == 2){
                            resultIE = temp;
                        }
                    }
                }

            } else if (text.contains("selenium-server-standalone")) {
                Driver temp = new Driver();
                temp.setName(name);
                temp.setOs(".");
                temp.setUrl(urlDrive);
                temp.setVersion(version);
                driversSelenium.add(temp);

                if (resultSelenium == null){
                    resultSelenium = temp;
                } else{
                    String verRe = resultSelenium.getVersion();
                    String verTemp = temp.getVersion();
                    if (verRe.contains("-beta")){
                        verRe = verRe.replace("-beta", ".");
                    }
                    if (verTemp.contains("-beta")){
                        verTemp = verRe.replace("-beta", ".");
                    }

                    if (compareVerison(verRe, verTemp) == 2){
                        resultSelenium = temp;
                    }
                }

            }
        }

//        writeToJson(driversIE, "ieDriver.json");
//        writeToJson(driversSelenium, "seleniumDriver.json");
        System.out.println("Done!");

        List<Driver> re = new ArrayList<>();
        re.add(resultIE);
        re.add(resultSelenium);
        return re;
    }

    public static Driver getGeckoDriver(String currentOS) throws IOException{
		String url = "https://github.com/mozilla/geckoDriver/releases/latest";

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("content-type", "application/github.v3+json");

        HttpResponse result;
		result = httpClient.execute(request);
		String json = EntityUtils.toString(result.getEntity(), "UTF-8");
		
		while(true){
			int index = json.indexOf("mozilla/geckodriver/releases/download");
			if(index == -1) break;
			int lastIndex = json.indexOf("\"", index);
			String urlTemp = json.substring(index, lastIndex);
			json = json.replace(urlTemp, "");
			
            if (urlTemp.contains(currentOS)){
                Driver gecko = new Driver();
                gecko.setName(urlTemp.substring(urlTemp.lastIndexOf("/")));
                gecko.setOs(currentOS);
                gecko.setUrl("https://github.com/" + urlTemp);
                gecko.setVersion(urlTemp.substring(urlTemp.indexOf("download/") + 10, urlTemp.lastIndexOf("/")));
               
                return gecko;
            }
			
		}
		return null;
    }
    
    @SuppressWarnings("unused")
	private static void writeToJson(List<Driver> drivers, String fileJson) throws IOException {
        Map<String, List<Driver>> dictionary = new HashMap<>();
        for (Driver i : drivers) {
            String version = i.getVersion();
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
            Driver chromeDriver = getChromeDrive("win64");

            List<Driver> re = getIEDriverSeleniumDriver("win64");
            Driver getGeckoDriver = getGeckoDriver("win64");

            System.out.println(chromeDriver.toString());

            System.out.println(re.get(0).toString());
            System.out.println(re.get(1).toString());

            System.out.println(getGeckoDriver.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
   
            
    }
}