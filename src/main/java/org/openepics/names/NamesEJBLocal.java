/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.names;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Local
public interface NamesEJBLocal {
    public boolean isEditor(String user);
    public List<NameEvent> getAllEvents();
    public void processEvents(NameEvent[] nevents, char status, String comment) throws Exception;
    public NameEvent createNewEvent(char eventType, String nameId, String category, 
              String code, String desription, String comment) throws Exception ;
    // public NameEvent cancelEvent(int eventId, String user, String comment);
    public List<NameEvent> getUnprocessedEvents();
    public List<NameEvent> getStandardNames(String category);    
    public List<NameEvent> findEventsByName(String nameId);
    public List<NameEvent> getUserRequests();
    public List<NameEvent> getApprovedNames();
    public void cancelRequest(int eventId, String comment) throws Exception;
    public List<NameEvent> findEvents(char eventType, char eventStatus);
}
