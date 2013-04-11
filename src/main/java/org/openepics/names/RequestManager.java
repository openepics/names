/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.names;

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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@ManagedBean
@ViewScoped
public class RequestManager implements Serializable {
    @EJB
    private NamesEJBLocal namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    private List<NameEvent> standardNames;
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
    
    private static final Map<String,String> statusNames;
    static {
        Map<String,String> map = new HashMap<String,String>();
        map.put("a", "Approved");
        map.put("c", "Cancelled");
        map.put("r", "Rejected");
        map.put("p", "Being Processed");
        statusNames = Collections.unmodifiableMap(map);
    }
           
    /**
     * Creates a new instance of RequestManager
     */
    public RequestManager() {
    }
    
    @PostConstruct
    public void init() {
        try {
            if (option == null) option = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("option");
            
            if (option == null) {
                standardNames = namesEJB.getApprovedNames(); // ToDo; check 'user'
                myRequest = false;
            } else if ("user".equals(option)) {
                standardNames = namesEJB.getUserRequests();
                myRequest = true;
            }
            newCategory = newCode = newDescription = newComment = null;
            selectedName = standardNames == null? null : standardNames.get(0);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not initialize NameManager.");
            System.err.println(e);
        }
    }

    public void onModify() {
        NameEvent newRequest;
        
        try {
            logger.log(Level.INFO, "Modifying ");
            newRequest = namesEJB.createNewEvent('m', selectedName.getNameId(), newCategory, newCode, newDescription, newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request was subbessfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }
    
    public void onAdd() {
        NameEvent newRequest;
        
        try {
            logger.log(Level.INFO, "Adding ");
            if ( newCode == null || newCode.isEmpty()) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Code is empty", " ");
            }
            newRequest = namesEJB.createNewEvent('i', "", newCategory, newCode, newDescription, newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request was subbessfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }

    public void onDelete() {
        NameEvent newRequest;
        
        try {
            if (selectedName == null ) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error:", "You did not select any name.");
                return;
            }
            
            logger.log(Level.INFO, "Deleting ");
            newRequest = namesEJB.createNewEvent('d', selectedName.getNameId(), 
                    selectedName.getNameCategoryId().getId(), selectedName.getNameCode(), 
                    selectedName.getNameDescription(), newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request was subbessfully submitted.", "Request Number: " + newRequest.getId());
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Encountered an error", e.getMessage());
            System.err.println(e);
        } finally {
            init();
        }
    }
    
    public void onCancel() {
        
        try {
            if (selectedName == null ) {
                showMessage(FacesMessage.SEVERITY_ERROR, "Error:", "You did not select any request.");
                return;
            }          
            logger.log(Level.INFO, "Cancelling ");
            namesEJB.cancelRequest(selectedName.getId(), newComment);
            showMessage(FacesMessage.SEVERITY_INFO, "Your request has been cancelled.", "Request Number: " );
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

    public String statusName(char s) {
        String status = statusNames.get(String.valueOf(s));
        if (status == null ) {
            status = "Invalid Status";
        }
        return status;
    }
    
    // ToDo: merge with same method in NamesManager
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
    /* --------------------------- */

    public List<NameEvent> getStandardNames() {
        return standardNames;
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
