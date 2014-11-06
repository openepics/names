/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openepics.discs.names.ui;

import org.openepics.discs.names.ent.NameRelease;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.names.ejb.NamesEJB;

/**
 *
 * @author vuppala
 */
@Named
@ViewScoped
public class NameReleaseConverter implements Converter {
    @EJB
    private NamesEJB namesEJB;
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
