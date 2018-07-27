/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Treatment;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class TreatmentFacade extends AbstractFacade<Treatment> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TreatmentFacade() {
        super(Treatment.class);
    }
    
    public List<Treatment> findAllAsc(){
        return getEntityManager().createQuery("select t from Treatment t ORDER BY t.block.level.parcel.trench.panel.id, t.block.level.parcel.trench.id, t.block.level.parcel.id, t.block.level.id, t.block.id, t.chronologicalOrder").getResultList();
    }
    
}
