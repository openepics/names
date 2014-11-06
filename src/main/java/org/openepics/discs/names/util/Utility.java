/*
 * This software is Copyright by the Board of Trustees of Michigan
 *  State University (c) Copyright 2013, 2014.
 *  
 *  
 *  You may use this software under the terms of the GNU public license
 *  (GPL). The terms of this license are described at:
 *    http://www.gnu.org/licenses/gpl.txt
 *  
 *  Contact Information:
 *       Facility for Rare Isotope Beam
 *       Michigan State University
 *       East Lansing, MI 48824-1321
 *        http://frib.msu.edu
 */
package org.openepics.discs.names.util;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.openepics.discs.names.ent.NameEvent;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
public class Utility {
    public static void showMessage(FacesMessage.Severity severity, String summary, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(severity, summary, message));
        // FacesMessage n = new FacesMessage();
    }  
    
    public static String nameStatus(NameEvent nreq, Date releaseDate) {
        String stat = "unknown";

        // Published: processed before the latest release, approved, and not a deletion request
        if (nreq.getProcessDate() != null
                && nreq.getProcessDate().before(releaseDate)
                && nreq.getStatus() == 'a' && nreq.getEventType() != 'd') {
            return "Published";
        }

        switch (nreq.getStatus()) {
            case 'p':
                stat = "In-Process";
                break;
            case 'c': // cancelled
                stat = "Cancelled";
                break;
            case 'r': // rejected
                stat = "Rejected";
                break;
            case 'a': // approved
                switch (nreq.getEventType()) {
                    case 'i': // add
                        stat = "Added";
                        break;
                    case 'm': // modify
                        stat = "Modified";
                        break;
                    case 'd': // delete
                        stat = "Deleted";
                        break;
                    default:
                        stat = "unknown";
                        break;
                }
                break;
        }

        return stat;
    }
}
