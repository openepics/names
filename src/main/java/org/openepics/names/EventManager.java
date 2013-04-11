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
@ManagedBean(name="eventManager")
@ViewScoped
public class EventManager implements Serializable {

    @EJB
    private NamesEJBLocal namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    private List<NameEvent> events;
    private NameEvent[] selectedEvents;
    private List<NameEvent> filteredEvents;
    
    // Input parameters
    private String procComments;

    /**
     * Creates a new instance of EventManager
     */
    public EventManager() {
    }

    @PostConstruct
    public void init() {
        try {
            events = namesEJB.getUnprocessedEvents();
            procComments = null;
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void onApprove() {
        try {
            logger.log(Level.INFO, "Approving ");
            namesEJB.processEvents(selectedEvents, 'a', procComments);
            showMessage(FacesMessage.SEVERITY_INFO, "All selected requests were subbessfully approved.", " ");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }

    public void onReject() {
        try {
            logger.log(Level.INFO, "Rejecting ");
            namesEJB.processEvents(selectedEvents, 'r', procComments);
            showMessage(FacesMessage.SEVERITY_INFO, "All selected requests were subbessfully rejected.", " ");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }

    private void showMessage(FacesMessage.Severity severity, String summary, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(severity, summary, message));
        FacesMessage n = new FacesMessage();
        
    }

    public NameEvent[] getSelectedEvents() {
        return selectedEvents;
    }

    public void setSelectedEvents(NameEvent[] selectedEvents) {
        this.selectedEvents = selectedEvents;
    }

    public List<NameEvent> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(List<NameEvent> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }

    public String getComments() {
        return procComments;
    }

    public void setComments(String comments) {
        this.procComments = comments;
    }

    public List<NameEvent> getEvents() {
        return events;
    }
}
