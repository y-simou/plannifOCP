/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Layer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class LayerFacade extends AbstractFacade<Layer> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LayerFacade() {
        super(Layer.class);
    }
    
     public void delete(Long layer){
        getEntityManager().createQuery("DELETE from ChemicalComponentLayer ch where ch.layer.id'"+ layer +"'").executeUpdate();
        remove(find(layer));
    }
     
     public Layer findByNomAndLevel(String nom,Long level){
        List<Layer> ps = getEntityManager().createQuery("SELECT p FROM Layer p where p.nom='"+ nom +"' AND p.level.id='"+ level +"'").getResultList();
        if(ps.isEmpty()){
            return null;
        }else{
            return ps.get(0);
        }
        
        
    }
    
}
