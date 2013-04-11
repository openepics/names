/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.names;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@ManagedBean
@ViewScoped
public class NamesManager implements Serializable {

    @EJB
    private NamesEJBLocal namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    private List<NameEvent> standardNames;
    private NameEvent selectedName;
    private List<NameEvent> filteredNames;
    private List<NameEvent> historyEvents;

    /**
     * Creates a new instance of NamesManager
     */
    public NamesManager() {
    }

    @PostConstruct
    public void init() {
        try {
            refreshNames();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not initialize NameManager.");
            System.err.println(e);
        }
    }

    private void refreshNames() {
        String category = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("category");
        if ( category == null ) {
            category = "%"; //ToDo: Use a better method. 
        }
        standardNames = namesEJB.getStandardNames(category);
    }

    public void findHistory() {
        try {
            if ( selectedName == null ) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must select a name first.");
                historyEvents = null;
                return;
            }
            logger.log(Level.INFO, "history ");
            historyEvents = namesEJB.findEventsByName(selectedName.getNameId());
            // showMessage(FacesMessage.SEVERITY_INFO, "Your request was subbessfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
           
        }
    }
    
    private void showMessage(FacesMessage.Severity severity, String summary, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(severity, summary, message));
        FacesMessage n = new FacesMessage();

    }
    
    public NameEvent getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(NameEvent selectedName) {
        this.selectedName = selectedName;
    }

    public List<NameEvent> getFilteredNames() {
        return filteredNames;
    }

    public void setFilteredNames(List<NameEvent> filteredNames) {
        this.filteredNames = filteredNames;
    }

    public List<NameEvent> getStandardNames() {
        return standardNames;
    }

    public List<NameEvent> getHistoryEvents() {
        return historyEvents;
    }
    
    
}
