/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.names.ui;

import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
//@Named(value = "loginManager")
@Named
@ViewScoped
public class LoginManager implements Serializable {

    @Inject private UserManager userManager;
    
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    @NotNull private String inputUserID;
    @NotNull private String inputPassword;
    private String originalURL;

    /**
     * Creates a new instance of LoginManager
     */
    public LoginManager() {
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        originalURL = (String) context.getExternalContext().getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        logger.log(Level.INFO, "Forwarded from: " + originalURL);
    }

    public String onLogin() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(inputUserID, inputPassword);
            logger.log(Level.INFO,"Login successful for " + inputUserID);
            RequestContext.getCurrentInstance().addCallbackParam("loginSuccess", true);
            // context.getExternalContext().getSessionMap().put("user", inputUserID);
            showMessage(FacesMessage.SEVERITY_INFO, "You are logged in. Welcome to Proteus.", inputUserID);
            if (originalURL != null) {
                context.getExternalContext().redirect(originalURL);
            }
        } catch (ServletException e) {          
            showMessage(FacesMessage.SEVERITY_ERROR, "Login Failed! Please try again. ", "Status: ");
            RequestContext.getCurrentInstance().addCallbackParam("loginSuccess", false);
        } finally {
            inputPassword = "xxxxxxxx"; // ToDo implement a better way destroy the password (from JVM)
            userManager.init();
        }
        return null;
        // return originalURL;
    }

    public String onLogout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            showMessage(FacesMessage.SEVERITY_INFO, "You have been logged out.", "Thank you!");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "Strangely, logout has failed", "That's odd!");
        } finally {
            userManager.init();
        }

        return "logout"; // ToDo: replace with null
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

    // ToDo: Move it to a common utility class
    private void showMessage(FacesMessage.Severity severity, String summary, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(severity, summary, message));
        // FacesMessage n = new FacesMessage();
    }
}
