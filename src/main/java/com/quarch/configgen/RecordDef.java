package com.quarch.configgen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="RecordDef")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class RecordDef {
    @XmlElement(required=true)
    public String Name;
    @XmlElement(required = true)
    public Integer Outputs;
    @XmlElement(name="Entry")
    public List<RecordEntry> entryList;
}

