/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.names.service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.openepics.names.NameEvent;
import org.openepics.names.NamesEJBLocal;
/**
 *
 * @author Vasu V <vuppala@frib.msu.org>
 */
@Stateless
@Path("name")
public class NameElementResource {
    @EJB
    private NamesEJBLocal namesEJB;

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
