package org.ghutchis.codegen;

import javax.xml.bind.annotation.XmlElement;

public class RecordEntry {
    @XmlElement(required=true)
    public String Name;
    public Integer Width;
}

