package com.katalon.plugin.drivers_updater;

import com.sun.xml.internal.txw2.annotation.XmlNamespace;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ListBucketResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListBucketResult {
//    @XmlElement(name = "Name")
//    private String Name;
//
//    @XmlElement(name = "Prefix")
//    private String Prefix;
//
//    @XmlElement(name = "Marker")
//    private String Marker;
//
//    @XmlElement(name = "IsTruncated")
//    private String IsTruncated;

    @XmlElement(name = "Contents")
    private List<Contents> Contents;


    public List<Contents> getContents() {
        return Contents;
    }

    public void setContents(List<Contents> contents) {
        Contents = contents;
    }

}
