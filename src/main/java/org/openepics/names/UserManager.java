/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.names;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
//import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.core.MultivaluedMap;
import org.openepics.auth.japi.AuthResponse;
import org.openepics.auth.japi.AuthServ;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Named("userManager")
@SessionScoped
public class UserManager implements Serializable {
    @EJB
    private NamesEJBLocal namesEJB;

    private String Ticket;
    private String User;
    private String inputUserID;
    private String inputPassword;
    private String  Role;
    private boolean LoggedIn = false;
    private boolean Editor = false;
    /**
     * Creates a new instance of UserManager
     */
    public UserManager() {
    }
     public String onLogin() {                  
        // RequestContext context = RequestContext.getCurrentInstance(); 
        AuthServ auth = new AuthServ("http://qa01.hlc.nscl.msu.edu:8080/auth/rs/v0");
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        AuthResponse resp;
        
        params.add("user", inputUserID);
        params.add("password", inputPassword);       
        resp = auth.authenticate(params);
        inputPassword = "xxxxxxxx"; // ToDo implement a better way destroy the password (from JVM)
        
        if ( resp.getStatus() == 0 ) {
            Ticket = resp.getTicket();
            LoggedIn = true;
            User = inputUserID;
            Editor = namesEJB.isEditor(User);
            showMessage("You are logged in. Welcome!", inputUserID);
        } else {
            Ticket = null;
            LoggedIn = false;
            User = null;
            Editor = false;
            showMessage("Login Failed! Please try again. ","Status: " + resp.getStatus());
        }  
        return null;
        // context.addCallbackParam("loggedIn", LoggedIn);
    }
    
    public String onLogout() {
        LoggedIn = false;
        Ticket = ""; 
        inputUserID = "";
        Editor = false;
        showMessage("You have been logged out.", "Thank you!");
        return  null;
    }
    
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

    private void showMessage(String summary, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(summary, message));
    }
}
