/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import bean.Treatment;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import service.Readxl;
import service.TreatmentFacade;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
@Path("gantt")
public class TreatmentFacadeREST {


    @EJB
    private TreatmentFacade treatmentFacade;
    private Readxl xlFacade;

//    @GET
//    @Path("operations")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<Treatment> operations() {
//        return xlFacade.findRange();
//    }
    
    
    @POST
    @Path("{create}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Treatment entity) {
        treatmentFacade.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Treatment entity) {
        treatmentFacade.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        treatmentFacade.remove(treatmentFacade.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Treatment find(@PathParam("id") Long id) {
        return treatmentFacade.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Treatment> findAll() {
        return treatmentFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Treatment> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return treatmentFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(treatmentFacade.count());
    }


}
