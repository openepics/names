/*
 * This software is Copyright by the Board of Trustees of Michigan
 * State University (c) Copyright 2012.
 * 
 * You may use this software under the terms of the GNU public license
 *  (GPL). The terms of this license are described at:
 *       http://www.gnu.org/licenses/gpl.txt
 * 
 * Contact Information:
 *   Facilitty for Rare Isotope Beam
 *   Michigan State University
 *   East Lansing, MI 48824-1321
 *   http://frib.msu.edu
 * 
 */
package org.openepics.names;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.MenuModel;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;

/**
 * For generating menu items for Naming Categories
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@ManagedBean
@ViewScoped
public class MenuManager implements Serializable {

    @EJB
    private NamesEJBLocal namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    private MenuModel model;
    private List<NameCategory> categories;

    /**
     * Creates a new instance of MenuManager
     */
    public MenuManager() {
    }

    @PostConstruct
    private void init() {
        model = new DefaultMenuModel();
        categories = namesEJB.getCategories();
        //menubar = new Menubar();
        //Submenu submenu = new Submenu();
        //submenu.setId("_category");
        //submenu.setLabel("Category");
        //submenu.setIcon("ui-icon-carat-2-e-w");

        //ToDo: Remove literal references to 'names.xhtml'. Use generic way.
        MenuItem item = new MenuItem();
        item.setId("_all");
        item.setValue("All");
        item.setUrl("/names.xhtml");
        model.addMenuItem(item);
        // ToDo: Move to facelets, if possible
        for (NameCategory cat : categories) {
            item = new MenuItem();
            item.setId("_" + cat.getId());
            item.setValue(cat.getName());
            item.setUrl("/names.xhtml?category=" + cat.getId());
            // submenu.getChildren().add(item);
            // item.setUpdate("@form");
            // menubar.getChildren().add(item);
            model.addMenuItem(item);
        }

        // submenu.getChildren().add(item);
        // model.addSubmenu(submenu);
        // menubar.getChildren().add(1, submenu);       
    }

    public List<NameCategory> getCategories() {
        return categories;
    }

    public MenuModel getModel() {
        return model;
    }
}
