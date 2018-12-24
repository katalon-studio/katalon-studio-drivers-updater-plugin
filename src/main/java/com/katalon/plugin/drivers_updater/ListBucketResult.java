package com.katalon.plugin.drivers_updater;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ListBucketResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListBucketResult {
    @XmlElement(name = "Contents")
    private List<Contents> Contents;

    public List<Contents> getContents() {
        return Contents;
    }

    public void setContents(List<Contents> contents) {
        Contents = contents;
    }

}
