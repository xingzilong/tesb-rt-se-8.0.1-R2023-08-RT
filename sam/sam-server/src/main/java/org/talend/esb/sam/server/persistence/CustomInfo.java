package org.talend.esb.sam.server.persistence;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomInfo {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
