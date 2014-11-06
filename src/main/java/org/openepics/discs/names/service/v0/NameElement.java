/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.names.service.v0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NameElement {
    private String id;
    private String code;
    private String description;
    private String category;
    
    NameElement() {       
    }
    
    NameElement(String id, String code, String category, String desc) {
        this.id = id;
        this.code = code;
        this.category = category;
        this.description = desc;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    } 
}
