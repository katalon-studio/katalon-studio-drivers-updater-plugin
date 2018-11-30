package com.katalon.plugin.drivers_updater;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static javax.xml.stream.XMLInputFactory.IS_NAMESPACE_AWARE;

public class Test {
    private static String url = "http://chromedriver.storage.googleapis.com/";

    private static ListBucketResult readData(String link){
        try {
            URL url = new URL(link);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            String out = "";
            while ((line = in.readLine()) != null){
//                System.out.println(line);
                out += line;
            }
            in.close();
            int index = out.indexOf("?>");

            out = out.replace("<?xml version='1.0' encoding='UTF-8'?>", "");

            JAXBContext context = JAXBContext.newInstance(ListBucketResult.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(IS_NAMESPACE_AWARE, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StringBufferInputStream(out));

            xsr = xif.createFilteredReader(xsr, reader -> {
                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                    return reader.getText().trim().length() > 0;
                }
                return true;
            });

            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ListBucketResult) unmarshaller.unmarshal(xsr);
        } catch (IOException | JAXBException | XMLStreamException e ){
            e.printStackTrace();
            return  null;
        }
    }

    public static void main(String[] args) {
        System.out.println(url);
        ListBucketResult a = readData(url);
        System.out.println(a.getContents());
    }
}
