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

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Manages report generation.
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@ManagedBean
@ViewScoped
public class ReportManager implements Serializable {

    @EJB
    private NamesEJBLocal namesEJB;
    private static final Logger logger = Logger.getLogger("org.openepics.names");
    private List<NameEvent> events;
    // Search Parameters
    private String eventType;
    private String eventStatus;
    private Date startDate, endDate;
    private String startRev, endRev;

    /**
     * Creates a new instance of ReportManager
     */
    public ReportManager() {
    }

    public void onGenReport() {
        try {
            logger.log(Level.INFO, "Action: generating report");
            // System.out.println("Action: generating report");
            char etype = eventType == null ? 0 : eventType.charAt(0);
            char estat = eventStatus == null ? 0 : eventStatus.charAt(0);
            events = namesEJB.findEvents(etype, estat);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            eventType = eventStatus = startRev = endRev = null;
            startDate = endDate = null;
        }
    }

    public List<NameEvent> getEvents() {
        return events;
    }

    public NamesEJBLocal getNamesEJB() {
        return namesEJB;
    }

    public void setNamesEJB(NamesEJBLocal namesEJB) {
        this.namesEJB = namesEJB;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartRev() {
        return startRev;
    }

    public void setStartRev(String startRev) {
        this.startRev = startRev;
    }

    public String getEndRev() {
        return endRev;
    }

    public void setEndRev(String endRev) {
        this.endRev = endRev;
    }
}
