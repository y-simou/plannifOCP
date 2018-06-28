/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Yassine.SIMOU
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
//        resources.add(rest.AxeFacadeREST.class);
//        resources.add(rest.BlockFacadeREST.class);
//        resources.add(rest.LevelLayerFacadeREST.class);
//        resources.add(rest.MachineFacadeREST.class);
//        resources.add(rest.OperationFacadeREST.class);
//        resources.add(rest.PanelFacadeREST.class);
//        resources.add(rest.ParcelFacadeREST.class);
//        resources.add(rest.SequenceLevelFacadeREST.class);
//        resources.add(rest.SiteFacadeREST.class);
//        resources.add(rest.SubPanelFacadeREST.class);
        resources.add(rest.AxeFacadeREST.class);
//        resources.add(rest.TrenchFacadeREST.class);
//        resources.add(rest.UnitOfTimeFacadeREST.class);
//        resources.add(rest.BlockFacadeREST.class);
//        resources.add(rest.LevelLayerFacadeREST.class);
//        resources.add(rest.MachineFacadeREST.class);
//        resources.add(rest.OperationFacadeREST.class);
//        resources.add(rest.PanelFacadeREST.class);
//        resources.add(rest.ParcelFacadeREST.class);
//        resources.add(rest.SequenceLevelFacadeREST.class);
//        resources.add(rest.SiteFacadeREST.class);
//        resources.add(rest.SubPanelFacadeREST.class);
//        resources.add(rest.TreatmentFacadeREST.class);
//        resources.add(rest.TrenchFacadeREST.class);
//        resources.add(rest.UnitOfTimeFacadeREST.class);
        resources.add(rest.BlockFacadeREST.class);
        resources.add(rest.LevelLayerFacadeREST.class);
        resources.add(rest.MachineFacadeREST.class);
        resources.add(rest.OperationFacadeREST.class);
        resources.add(rest.PanelFacadeREST.class);
        resources.add(rest.ParcelFacadeREST.class);
        resources.add(rest.SequenceLevelFacadeREST.class);
        resources.add(rest.SiteFacadeREST.class);
        resources.add(rest.SubPanelFacadeREST.class);
        resources.add(rest.TreatmentFacadeREST.class);
        resources.add(rest.TrenchFacadeREST.class);
        resources.add(rest.UnitOfTimeFacadeREST.class);
    }
    
}
