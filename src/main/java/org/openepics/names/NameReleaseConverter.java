/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openepics.names;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author vuppala
 */
@ManagedBean
public class NameReleaseConverter implements Converter {
    @EJB
    private NamesEJBLocal namesEJB;
     private static final Logger logger = Logger.getLogger("org.openepics.discs.conf");
     
     public NameReleaseConverter() {
         
     }
     
     @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        NameRelease dtype;

        if (value == null || value.equals("")) {
            logger.log(Level.INFO, "exp converter: empty experiemnt id");
            return null;
        } else {
            dtype = namesEJB.findRelease(value);
            return dtype;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            logger.log(Level.INFO, "Null object");
            return "";
        } else {
            // logger.log(Level.INFO, "Exp number: " + ((Experiment) value).getId().toString());
            return ((NameRelease) value).getId();
        }
    }
}
