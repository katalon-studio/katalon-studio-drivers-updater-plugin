package com.katalon.plugin.driver_upload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Contents")
@XmlAccessorType(XmlAccessType.NONE)
public class Contents {
    @XmlElement(name = "Key")
    private String Key;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }


    @Override
    public String toString() {

        return this.Key;
    }

}
