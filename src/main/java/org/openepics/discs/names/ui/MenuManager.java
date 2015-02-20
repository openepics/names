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
package org.openepics.discs.names.ui;

import org.openepics.discs.names.ent.NameCategory;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.names.ejb.NamesEJB;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuModel;

/**
 * For generating menu items for Naming Categories
 * TODO: Replace with data table drop-down filter
 * 
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Named
@ViewScoped
public class MenuManager implements Serializable {

    @EJB
    private NamesEJB namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    private DefaultMenuModel model;
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
        DefaultMenuItem item = new DefaultMenuItem();
        item.setId("_all");
        item.setValue("All");
        item.setUrl("/names.xhtml");
        model.addElement(item);
        // ToDo: Move to facelets, if possible
        for (NameCategory cat : categories) {
            item = new DefaultMenuItem();
            item.setId("_" + cat.getId());
            item.setValue(cat.getName());
            item.setUrl("/names.xhtml?category=" + cat.getId());
            // submenu.getChildren().add(item);
            // item.setUpdate("@form");
            // menubar.getChildren().add(item);
            model.addElement(item);
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
