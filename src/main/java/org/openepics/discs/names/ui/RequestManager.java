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

import org.openepics.discs.names.ent.NameEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.openepics.discs.names.ejb.NamesEJB;

/**
 * Manages Change Requests (backing bean for request-sub.xhtml)
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Named
@ViewScoped
public class RequestManager implements Serializable {

    @EJB
    private NamesEJB namesEJB;
    private static final Logger logger = Logger.getLogger(RequestManager.class.getName());
    private List<NameEvent> validNames;
    private NameEvent selectedName;
    private List<NameEvent> filteredNames;
    private List<NameEvent> historyEvents;
    private boolean myRequest = false; // user is looking at 'his'her' requests i.e. 'option' param is 'user'
    private String option = null; // option parameter 
    // Input parameters from input page
    private String newCategory;
    private String newCode;
    private String newDescription;
    private String newComment;
    private static final Map<String, String> requestTypeNames;

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("i", "Add");
        map.put("m", "Modify");
        map.put("d", "Delete");
        map.put("c", "Cancel");
        requestTypeNames = Collections.unmodifiableMap(map);
    }

    /**
     * Creates a new instance of RequestManager
     */
    public RequestManager() {
    }

    @PostConstruct
    public void init() {
        try {
            if (option == null) {
                option = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("option");
            }

            if (option == null) {
                validNames = namesEJB.getValidNames();
                myRequest = false;
            } else if ("user".equals(option)) {
                validNames = namesEJB.getUserRequests();
                myRequest = true;
            }
            newCategory = newCode = newDescription = newComment = null;
            selectedName = validNames == null ? null : validNames.get(0);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not initialize Request Manager.");
            System.err.println(e);
        }
    }

    /**
     * Do preparatory work for name modification.
     * TODO: Better to have a newName object of type NameEvent instead of all 
     *        the different variables
     */
    public void prepareForModify() {
        newCategory = selectedName.getNameCategoryId().getId();
        newCode = selectedName.getNameCode();
        newDescription = selectedName.getNameDescription();
        newComment = null;
    }
    
    /**
     * Modify a name.
     * 
     */
    public void onModify() {
        NameEvent newRequest;

        try {
            logger.log(Level.INFO, "Modifying ");
            newRequest = namesEJB.createNewEvent('m', selectedName.getNameId(), newCategory, newCode, newDescription, newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request was successfully submitted.", "Request Number: " + newRequest.getId());
//            showMessage(FacesMessage.SEVERITY_INFO, "Your request was successfully submitted.", "Request Number: " );
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }

    /**
     * Add a new name.
     */
    public void onAdd() {
        NameEvent newRequest;

        try {
            logger.log(Level.INFO, "Adding ");
            if (newCode == null || newCode.isEmpty()) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Code is empty", " ");
            }
            newRequest = namesEJB.createNewEvent('i', "", newCategory, newCode, newDescription, newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request was successfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }

    /*
     * Has the selectedName been processed?
     */
    public boolean selectedEventProcessed() {
        return selectedName == null ? false : selectedName.getStatus() != 'p';
    }

    /**
     * Delete a name.
     */
    public void onDelete() {
        NameEvent newRequest;

        try {
            if (selectedName == null) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error:", "You did not select any name.");
                return;
            }

            logger.log(Level.INFO, "Deleting ");
            newRequest = namesEJB.createNewEvent('d', selectedName.getNameId(),
                    selectedName.getNameCategoryId().getId(), selectedName.getNameCode(),
                    selectedName.getNameDescription(), newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request was successfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }

    public void onCancel() {

        try {
            if (selectedName == null) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error:", "You did not select any request.");
                return;
            }
            logger.log(Level.INFO, "Cancelling ");
            namesEJB.cancelRequest(selectedName.getId(), newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request has been cancelled.", "Request Number: ");
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

    /*
     * Convert a type code (p, a, r etc) to descriptive string
     */
    public String requestType(char s) {
        String tname = requestTypeNames.get(String.valueOf(s));
        if (tname == null) {
            tname = "Invalid Request Type";
        }
        return tname;
    }

    // ToDo: merge with same method in NamesManager
    public void findHistory() {
        try {
            if (selectedName == null) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error", "You must select a name first.");
                historyEvents = null;
                return;
            }
            logger.log(Level.INFO, "history ");
            historyEvents = namesEJB.findEventsByName(selectedName.getNameId());
            // showMessage(FacesMessage.SEVERITY_INFO, "Your request was successfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
        }
    }
    /* --------------------------- */

    public List<NameEvent> getValidNames() {
        return validNames;
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

    public String getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(String newCategory) {
        this.newCategory = newCategory;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public boolean isMyRequest() {
        return myRequest;
    }

    public List<NameEvent> getHistoryEvents() {
        return historyEvents;
    }
}
