/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.names.service.v1;

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
    private String status;
    
    NameElement() {       
    }
    
    NameElement(String id, String code, String category, String desc, String status) {
        this.id = id;
        this.code = code;
        this.category = category;
        this.description = desc;
        this.status = status;
    }  

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
