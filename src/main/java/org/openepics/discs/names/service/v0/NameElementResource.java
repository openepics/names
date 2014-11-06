/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.names.service.v0;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.openepics.discs.names.ejb.NamesEJB;
import org.openepics.discs.names.ent.NameEvent;
/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Path("/v0/name")
public class NameElementResource {
    @EJB
    private NamesEJB namesEJB;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public List<NameElement> getNameElements(@DefaultValue("%") @QueryParam("category") String categoty,
        @DefaultValue("false") @QueryParam("deleted") Boolean deleted) {
        List<NameEvent> nameEvents;
        List<NameElement> nameElements = new ArrayList();
        
        nameEvents = namesEJB.getStandardNames(categoty, deleted);
        
        for (NameEvent ne: nameEvents) {
            nameElements.add(new NameElement(ne.getNameId(), ne.getNameCode(), ne.getNameCategoryId().getId(), ne.getNameDescription()));
        }
        return nameElements;
    }
    
}
