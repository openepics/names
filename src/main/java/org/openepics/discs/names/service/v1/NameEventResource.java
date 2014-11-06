/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openepics.discs.names.service.v1;

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
@Path("/v1/event")
public class NameEventResource  {
    @EJB
    private NamesEJB namesEJB;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public List<NameEvent> getNameElements(@DefaultValue("%") @QueryParam("category") String categoty,
        @DefaultValue("false") @QueryParam("deleted") Boolean deleted) {
       
        return namesEJB.getStandardNames(categoty, deleted);
    }
    
    /*
    public NameEventResource() {
        super(NameEvent.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(NameEvent entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(NameEvent entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public NameEvent find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<NameEvent> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<NameEvent> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    */
}
