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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Named
@SessionScoped
public class UserManager implements Serializable {

    @EJB
    private NamesEJBLocal namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    
    private String Ticket;
    private String User;
    private String inputUserID;
    private String inputPassword;
    private String Role;
    private boolean LoggedIn = false;
    private boolean Editor = false;

    /**
     * Creates a new instance of UserManager
     */
    public UserManager() {
    }

    public String onLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // ToDo: find an alternative for this workaround
        // Sometimes the user is already logged in. This is a workaround for it. 
        try {
            request.logout();
        } catch (Exception e) {
            logger.log(Level.INFO, "Cannot log out during login");
        }
        
        try {
            // resp = namesEJB.authenticate(inputUserID, inputPassword);

            request.login(this.inputUserID, this.inputPassword);
            inputPassword = "xxxxxxxx"; // ToDo implement a better way destroy the password (from JVM)
            LoggedIn = true;
            User = inputUserID;
            Editor = namesEJB.isEditor(User);
            showMessage(FacesMessage.SEVERITY_INFO, "You are logged in. Welcome to Proteus.", inputUserID);

        } catch (Exception e) {
            Ticket = null;
            LoggedIn = false;
            User = null;
            Editor = false;
            showMessage(FacesMessage.SEVERITY_ERROR, "Login Failed! Please try again. ", "Status: ");

        } finally {
        }
        return null;
    }

    public String onLogout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            LoggedIn = false;
            Ticket = "";
            inputUserID = "";
            Editor = false;
            showMessage(FacesMessage.SEVERITY_INFO, "You have been logged out.", "Thank you!");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Strangely, logout has failed", "That's odd!");
        }

        return "/index.xhtml";
    }

//    public String onLogin() {
//        AuthResponse resp;
//
//        try {
//            resp = namesEJB.authenticate(inputUserID, inputPassword);
//            inputPassword = "xxxxxxxx"; // ToDo implement a better way destroy the password (from JVM)
//            if (resp == null) {
//                showMessage(FacesMessage.SEVERITY_ERROR, "This is embarassing; cannot authenticate you.", "Most probably authenticate service is not configured.");
//            }
//            if (resp.getStatus() == 0) {
//                Ticket = resp.getTicket();
//                LoggedIn = true;
//                User = inputUserID;
//                Editor = namesEJB.isEditor(User);
//                showMessage(FacesMessage.SEVERITY_INFO, "You are logged in. Welcome to Proteus.", inputUserID);
//            } else {
//                Ticket = null;
//                LoggedIn = false;
//                User = null;
//                Editor = false;
//                showMessage(FacesMessage.SEVERITY_ERROR, "Login Failed! Please try again. ", "Status: " + resp.getStatus());
//            }
//        } catch (Exception e) {
//        } finally {
//        }
//        return null;
//    }
//
//    public String onLogout() {
//        LoggedIn = false;
//        Ticket = "";
//        inputUserID = "";
//        Editor = false;
//        showMessage(FacesMessage.SEVERITY_INFO, "You have been logged out.", "Thank you!");
//        return null;
//    }
    public String getTicket() {
        return Ticket;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getInputUserID() {
        return inputUserID;
    }

    public void setInputUserID(String inputUserID) {
        this.inputUserID = inputUserID;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public String getUser() {
        return User;
    }

    public boolean isLoggedIn() {
        return LoggedIn;
    }

    public boolean isEditor() {
        return Editor;
    }

    // ToDo: Move it to a common utility class
    private void showMessage(FacesMessage.Severity severity, String summary, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(severity, summary, message));
        FacesMessage n = new FacesMessage();
    }
}
