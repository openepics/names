/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.names.service.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.openepics.discs.names.ejb.NamesEJB;
import org.openepics.discs.names.ent.NameEvent;
import org.openepics.discs.names.ent.NameRelease;
import org.openepics.discs.names.util.Utility;

/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */

@Path("/v1/name")
public class NameElementResource {

    @EJB
    private NamesEJB namesEJB;   

    private static final Logger logger = Logger.getLogger(NameElementResource.class.getName());

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public List<NameElement> getNameElements(@DefaultValue("%") @QueryParam("category") String categoty,
            @DefaultValue("false") @QueryParam("all") Boolean includeAll) {
        List<NameEvent> nameEvents;
        List<NameElement> nameElements = new ArrayList();

        nameEvents = namesEJB.findNames(categoty, includeAll);

        for (NameEvent ne : nameEvents) {
            nameElements.add(new NameElement(ne.getNameId(), ne.getNameCode(), ne.getNameCategoryId().getId(), ne.getNameDescription(), findStatus(ne)));
        }
        return nameElements;
    }

    /*
     *  Compose the status of a name event. 
    */
    private String findStatus(NameEvent nreq) {       
        NameRelease latestRelease;
        List<NameRelease> releases;
        Date releaseDate;

        releases = namesEJB.getAllReleases();

        if (releases != null && !releases.isEmpty()) {            
            releaseDate = releases.get(0).getReleaseDate();         // releases are assumed in descending order of release date         
        } else {
            logger.log(Level.WARNING, "No release information in the database!");
            releaseDate = new Date();
        }
        // Published: processed before the latest release, approved, and not a deletion request      

        return Utility.nameStatus(nreq, releaseDate);
    }

}
