/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SequenceLevel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class SequenceLevelFacade extends AbstractFacade<SequenceLevel> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SequenceLevelFacade() {
        super(SequenceLevel.class);
    }
    
     public void delete(Long sn){
        getEntityManager().createQuery("DELETE FROM CompositionLevelSequence cls where cls.sequenceLevel.id'"+ sn +"'").executeUpdate();
        remove(find(sn));
    }
    
}
