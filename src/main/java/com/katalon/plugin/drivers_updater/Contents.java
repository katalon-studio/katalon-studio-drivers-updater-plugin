package com.katalon.plugin.drivers_updater;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Contents")
@XmlAccessorType(XmlAccessType.NONE)
public class Contents {
    @XmlElement(name = "Key")
    private String Key;
//
//    @XmlElement(name = "Generation")
//    private String Generation;
//
//    @XmlElement(name = "MetaGeneration")
//    private String MetaGeneration;
//
//    @XmlElement(name = "LastModified")
//    private String LastModified;
//
//    @XmlElement(name = "ETag")
//    private String ETag;
//
//    @XmlElement(name = "Size")
//    private String Size;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

//    public String getGeneration() {
//        return Generation;
//    }
//
//    public void setGeneration(String generation) {
//        Generation = generation;
//    }
//
//    public String getMetaGeneration() {
//        return MetaGeneration;
//    }
//
//    public void setMetaGeneration(String metaGeneration) {
//        MetaGeneration = metaGeneration;
//    }
//
//    public String getLastModified() {
//        return LastModified;
//    }
//
//    public void setLastModified(String lastModified) {
//        LastModified = lastModified;
//    }
//
//    public String getETag() {
//        return ETag;
//    }
//
//    public void setETag(String etag) {
//        ETag = etag;
//    }
//
//    public String getSize() {
//        return Size;
//    }
//
//    public void setSize(String size) {
//        Size = size;
//    }

    @Override
    public String toString() {

        return this.Key;// + " "  + this.Generation + " " + this.MetaGeneration + " " +
//                this.LastModified + "  " + this.ETag + " " + this.Size;
    }

}
