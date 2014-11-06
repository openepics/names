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
import org.openepics.discs.names.ent.NameRelease;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.openepics.discs.names.ejb.NamesEJB;

/**
 * Manages report generation.
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Named
@ViewScoped
public class ReportManager implements Serializable {

    @EJB
    private NamesEJB namesEJB;
    @Inject
    private PublicationManager pubManager;
    
    private static final Logger logger = Logger.getLogger(ReportManager.class.getName());
    
    private List<NameEvent> events;
    //private List<NameEvent> standardNames;
    // Search Parameters
    private String eventType;
    private String eventStatus;
    private Date startDate, endDate;
    // private String startRev, endRev;   
    private NameRelease inRelease;

    /**
     * Creates a new instance of ReportManager
     */
    public ReportManager() {
    }

    public void onGenReport() {
        try {
            logger.log(Level.INFO, "Action: generating report");
            // System.out.println("Action: generating report");
            char etype = "%".equals(eventType) ? 0 : eventType.charAt(0);
            char estat = "%".equals(eventStatus) ? 0 : eventStatus.charAt(0);
            events = namesEJB.findEvents(etype, estat);
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            eventType = eventStatus = null;
            // eventType = eventStatus = startRev = endRev = null;
            startDate = endDate = null;
        }
    }

    public void approvedNamesReport() {
        List<NameEvent> stdnames;
        Date relDate;

        try {
            if (events == null) {
                events = new ArrayList<NameEvent>();
            } else {
                events.clear();
            }

            logger.log(Level.INFO, "Action: generating published report");
            // System.out.println("Action: generating report");
            stdnames = namesEJB.getStandardNames("%", false);
            relDate = inRelease == null ? new Date() : inRelease.getReleaseDate();

            for (NameEvent nreq : stdnames) {
                if (nreq.getProcessDate() != null
                        && nreq.getProcessDate().before(relDate)
                        && nreq.getStatus() == 'a' && nreq.getEventType() != 'd') {
                    // it is published
                    events.add(nreq);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            eventType = eventStatus = null;
            //eventType = eventStatus = startRev = endRev = null;
            startDate = endDate = null;
            inRelease = null;
        }
    }

    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.setPageSize(PageSize.LETTER.rotate());
        // pdf.open();
        // pdf.setPageSize(PageSize.LETTER.rotate());
        pdf.addCreationDate();
        pdf.addHeader("Header", "Proteus: Naming Convention Rep");
        pdf.addTitle("Proteus: Naming Convention Report");

        // ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        // String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "prime_logo.png";

        // pdf.add(Image.getInstance(logo));
    }
    
    public List<NameEvent> getEvents() {
        return events;
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

    public NameRelease getInRelease() {
        return inRelease;
    }

    public void setInRelease(NameRelease inRelease) {
        this.inRelease = inRelease;
    }

    public void setPubManager(PublicationManager pubMgr) {
        this.pubManager = pubMgr;
    }

}
