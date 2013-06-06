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

import java.util.List;
import javax.ejb.Local;
// import org.openepics.auth.japi.AuthResponse;

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
    public List<NameEvent> getUnprocessedEvents();
    public List<NameEvent> getStandardNames(String category, boolean includeDeleted);    
    public List<NameEvent> findEventsByName(String nameId);
    public List<NameEvent> getUserRequests();
    public List<NameEvent> getValidNames();
    public void cancelRequest(int eventId, String comment) throws Exception;
    public List<NameEvent> findEvents(char eventType, char eventStatus);
    public List<NameRelease> getAllReleases();
    public NameRelease createNewRelease(NameRelease newRelease) throws Exception;
    public boolean isUnderChange(NameEvent nevent);
    public NameEvent findLatestEvent(String nameId);
    // public AuthResponse authenticate(String userid, String password) throws Exception;
    public List<NameCategory> getCategories();
}
