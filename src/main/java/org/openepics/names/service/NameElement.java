/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.names.service;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@XmlRootElement
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

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    
}
