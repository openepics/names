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

import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MultivaluedMap;
import org.openepics.auth.japi.*;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Stateless
public class NamesEJB implements NamesEJBLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    
    @Inject
    private UserManager userManager;
    
    @PersistenceContext(unitName = "org.openepics.names.punit")
    private EntityManager em;
    
    private AuthServ authService = null; //* Authentication service
   
    @Override
    public NameEvent createNewEvent(char eventType, String nameId, String category,
            String code, String description, String comment) throws Exception {
        logger.log(Level.INFO, "creating...");
        Date curdate = new Date();

        if (userManager == null) {
            throw new Exception("userManager is null. Cannot inject it");
        }

        if ( !userManager.isLoggedIn()) {
            throw new Exception("You are not authorized to perform this operation.");
        }
        //NameCategory ncat = new NameCategory(category, category,0);
        NameCategory ncat;
        ncat = em.find(NameCategory.class, category);
        if (ncat == null) {
            logger.log(Level.SEVERE, "Invalid categroy: " + category);
            return null;
        }
        NameEvent mEvent = new NameEvent(0, eventType, userManager.getUser(), curdate, 'p', code, description, 0);
        logger.log(Level.INFO, "new created:" + code + ":" + description);
        if (eventType == 'i') { // initiation/insert. 
            nameId = UUID.randomUUID().toString();
        }

        mEvent.setRequestorComment(comment);
        mEvent.setNameCategoryId(ncat);
        mEvent.setNameId(nameId);
        logger.log(Level.INFO, "set properties...");
        em.persist(mEvent);
        logger.log(Level.INFO, "persisted...");
        return mEvent;
    }
    
    @Override
    public NameRelease createNewRelease(NameRelease newRelease) throws Exception {
        logger.log(Level.INFO, "creating release...");       

        if (!userManager.isEditor()) {
            throw new Exception("You are not authorized to perform this operation.");
        }
        newRelease.setReleaseDate(new Date());
        // logger.log(Level.INFO, "set properties...");
        em.persist(newRelease);
        logger.log(Level.INFO, "published new release ...");
        return newRelease;
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameEvent> getUnprocessedEvents() {
        List<NameEvent> nameEvents;

        TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n WHERE n.status = 'p'", NameEvent.class); // ToDo: convert to criteria query
        nameEvents = query.getResultList();
        logger.log(Level.INFO, "retreived pending requests: " + nameEvents.size());
        return nameEvents;
    }

    /*
     * Events that are approved, and are new ('i') or modified ('m').
     */
    @Override
    public List<NameEvent> getValidNames() {       
        return getStandardNames("%",false);
    }
    
    /*
     * Is name being changed?
     */
    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public boolean isUnderChange(NameEvent nevent) {
        List<NameEvent> nameEvents;

        TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n WHERE n.nameId = :id AND n.status != 'r' AND  n.requestDate > (SELECT MAX(r.releaseDate) FROM NameRelease r)", NameEvent.class)
                .setParameter("id", nevent.getNameId());
        nameEvents = query.getResultList();
        logger.log(Level.INFO, "change requests: " + nameEvents.size());
        return ! nameEvents.isEmpty();
    }

    /*
     * Events that are approved, and are new ('i') or modified ('m').
     */
    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameEvent> getUserRequests() {
        List<NameEvent> nameEvents;
        String user = userManager.getUser();
        // String user = "system";

        if (user == null) {
            return null;
        }

        TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n WHERE n.requestedBy = :user", NameEvent.class)
                .setParameter("user", user);

        nameEvents = query.getResultList();
        logger.log(Level.INFO, "Results for requests: " + nameEvents.size());
        return nameEvents;
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameEvent> getAllEvents() {
        List<NameEvent> nameEvents;

        TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n ORDER BY n.requestDate DESC", NameEvent.class); // ToDo: convert to criteria query.      
        nameEvents = query.getResultList();
        logger.log(Level.INFO, "Results for all events: " + nameEvents.size());
        return nameEvents;
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameRelease> getAllReleases() {
        List<NameRelease> releases;

        TypedQuery<NameRelease> query = em.createQuery("SELECT n FROM NameRelease n ORDER BY n.releaseDate DESC", NameRelease.class); // ToDo: convert to criteria query.      
        releases = query.getResultList();
        logger.log(Level.INFO, "Results for all events: " + releases.size());
        return releases;
    }
    
    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameEvent> findEventsByName(String nameId) {
        List<NameEvent> nameEvents;

        TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n WHERE n.nameId = :nameid ORDER BY n.requestDate DESC", NameEvent.class)
                .setParameter("nameid", nameId); // ToDo: convert to criteria query.      
        nameEvents = query.getResultList();
        logger.log(Level.INFO, "Events for " + nameId + nameEvents.size());
        return nameEvents;
    }

    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public NameEvent findLatestEvent(String nameId) {
        List<NameEvent> nameEvents;

        TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n WHERE n.nameId = :nameid  AND n.status != 'r' ORDER BY n.requestDate DESC", NameEvent.class)
                .setParameter("nameid", nameId); // ToDo: convert to criteria query.      
        nameEvents = query.getResultList();     
        // logger.log(Level.INFO, "Events for " + nameId + nameEvents.size());
        if (nameEvents.isEmpty()) {
            return null;
        } else {
            return nameEvents.get(0);
        }
    }
    
    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameEvent> findEvents(char eventType, char eventStatus) {
        List<NameEvent> nameEvents;

        String queryStr = "SELECT n FROM NameEvent n ";
        String cons = "";
        
        if (eventType != 0 ) {
            cons += " n.eventType = '" + String.valueOf(eventType) + "' "; // ToDo: Bad idea! change to criteria query
        }
        if (! "".equals(cons)) {
            cons += " AND ";
        }
        if (eventStatus != 0) {
            cons += " n.status = '" + String.valueOf(eventStatus) + "' "; // ToDo: Bad idea! change to criteria query
        }

        if ( !"".equals(cons)) {
            queryStr += "WHERE " + cons;
        }  
        
        logger.log(Level.INFO, "Search query is: " + queryStr);
        
        TypedQuery<NameEvent> query = em.createQuery(queryStr, NameEvent.class); // ToDo: convert to criteria query.  
     
        nameEvents = query.getResultList();
        logger.log(Level.INFO, "Search hits: "  + nameEvents.size());
        return nameEvents;
    }
    /*
     * Events that are approved, and are new ('i') or modified ('m').
     */
    @Override
    // @TransactionAttribute(TransactionAttributeType.SUPPORTS) // No transaction as it is read-only query
    public List<NameEvent> getStandardNames(String category, boolean includeDeleted) {
        List<NameEvent> nameEvents;
        TypedQuery<NameEvent> query;
        
        //TypedQuery<NameEvent> query = em.createQuery("SELECT n FROM NameEvent n WHERE n.status = 'a' AND n.eventType IN (?1, ?2) AND n.nameCategoryId.id LIKE ?3 AND n.processDate <= (SELECT MAX(r.releaseDate) FROM NameRelease r) ORDER BY n.nameCategoryId.id, n.nameCode", NameEvent.class)
        if (includeDeleted) {
            query = em.createQuery("SELECT n FROM NameEvent n WHERE n.nameCategoryId.id LIKE :categ AND n.requestDate = (SELECT MAX(r.requestDate) FROM NameEvent r WHERE r.nameId = n.nameId AND (r.status = 'a' OR r.status = 'p')) ORDER BY n.nameCategoryId.id, n.nameCode", NameEvent.class)
                    .setParameter("categ", category); // ToDo: convert to criteria query.
        } else {
            query = em.createQuery("SELECT n FROM NameEvent n WHERE n.nameCategoryId.id LIKE :categ AND n.requestDate = (SELECT MAX(r.requestDate) FROM NameEvent r WHERE r.nameId = n.nameId AND (r.status = 'a' OR r.status = 'p')) AND NOT (n.eventType = 'd' AND n.status = 'a') ORDER BY n.nameCategoryId.id, n.nameCode", NameEvent.class)
                    .setParameter("categ", category); // ToDo: convert to criteria query.
        }
        // ToDo: check category values
        nameEvents = query.getResultList();
        logger.log(Level.INFO, "Results for category " + category + ":" + nameEvents.size());
        return nameEvents;
    }

    @Override
    public void processEvents(NameEvent[] nevents, char status,
            String comment) throws Exception {
        NameEvent mEvent;

        // ToDO; check if user has privs to processEvents.
        if (!userManager.isEditor()) {
            throw new Exception("You are not authorized to perform this operation.");
        }
        logger.log(Level.INFO, "Processing events " + nevents.length);
        for (NameEvent event : nevents) {
            logger.log(Level.INFO, "Processing  " + event.getNameCode());
            mEvent = em.find(NameEvent.class, event.getId(), LockModeType.OPTIMISTIC); // ToDo: better to merge or extend the persistence context?
            mEvent.setStatus(status);
            mEvent.setProcessDate(new java.util.Date());
            mEvent.setProcessorComment(comment);
            mEvent.setProcessedBy(userManager.getUser());
            if (status == 'r') { // rejected. ToDO: use enums.
                continue;
            }
            if (event.getEventType() == 'i') { // initiated. ToDO: use enums. 
                mEvent.setNameId(UUID.randomUUID().toString());
            }
        }
    }

    @Override
    public void cancelRequest(int eventId, String comment) throws Exception {
        NameEvent mEvent;

        // ToDO; check if user has privs to processEvents.       
        mEvent = em.find(NameEvent.class, eventId, LockModeType.OPTIMISTIC); // ToDo: better to merge or extend the persistence context?

        if (mEvent == null) {
            throw new Exception("Event not found.");
        }
        if (!userManager.isEditor() && !mEvent.getRequestedBy().equals(userManager.getUser())) {
            throw new Exception("Unauthorized access");
        }
        logger.log(Level.INFO, "Processing  " + mEvent.getNameCode());
        mEvent.setStatus('c'); // cancelled
        mEvent.setProcessDate(new java.util.Date());
        mEvent.setProcessorComment(comment);
        mEvent.setProcessedBy(userManager.getUser());

    }
    
    @Override
    // TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean isEditor(String user) {
        Privilege priv = em.find(Privilege.class, user);

        if (priv != null) {
            return "E".equals(priv.getOperation());
        } else {
            return false;
        }
    }

    private int validate(String ticket) throws Exception {
        findAuthService();

        if (authService == null) {
            logger.log(Level.WARNING, "Cannot access Auth Service.");
            return -1; // ToDo: This is not good. Use exceptions.
        }

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        AuthResponse resp;

        params.add("ticket", ticket);

        resp = authService.validate(params);

        return resp.getStatus();
    }
    
    @Override
    public AuthResponse authenticate (String userid, String password) throws Exception {
        AuthResponse response;
        findAuthService();

        if (authService == null) {
            logger.log(Level.WARNING, "Cannot access Auth Service.");
            return null; // ToDo: Use exceptions.
        }
        
        // RequestContext context = RequestContext.getCurrentInstance(); 
        // AuthServ auth = new AuthServ("http://qa01.hlc.nscl.msu.edu:8080/auth/rs/v0");
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();        

        params.add("user", userid);
        params.add("password", password);
        response = authService.authenticate(params);
        password = "xxxxxxxx"; // ToDo implement a better way destroy the password (from JVM)

        return response;
    }

    private void findAuthService() throws Exception {
        Properties prop = getProperties("AuthDomain"); // defined via JNDI
        String serviceURL;

        if (prop == null || !"true".equals(prop.getProperty("Enabled"))) {
            authService = null;
            logger.log(Level.INFO, "Auth Domain not enabled or defined");
            return;
        }

        if (authService == null) {
            serviceURL = prop.getProperty("ServiceURL");
            if (serviceURL == null || serviceURL.isEmpty()) {
                logger.log(Level.SEVERE, "ServiceURL not set");
                authService = null;
            } else {
                authService = new AuthServ(serviceURL);
            }
        }
    }

    private Properties getProperties(String jndiName) throws Exception {
        Properties properties;

        InitialContext context = new InitialContext();
        properties = (Properties) context.lookup(jndiName);
        context.close();

        if (properties == null) {
            logger.log(Level.SEVERE, "Error occurred while getting properties from JNDI.");
        }

        return properties;
    }
    
}
