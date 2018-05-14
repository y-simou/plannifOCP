/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.LevelLayer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class LevelLayerFacade extends AbstractFacade<LevelLayer> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LevelLayerFacade() {
        super(LevelLayer.class);
    }
    
    public void delete(LevelLayer levelLayer){
        getEntityManager().createQuery("DELETE from ChemicalComponentLayer ch where ch.layer.level.num='"+ levelLayer.getNum() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE from Layer l where l.level.num='"+ levelLayer.getNum() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE from Storage st where st.block.level.num='" + levelLayer.getNum() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Accessibility a where a.levelLayer.num='" + levelLayer.getNum() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from StatutBlock s where s.block.level.num='" + levelLayer.getNum() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Treatment t where t.block.level.num='" + levelLayer.getNum() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Block b where b.level.num='"+ levelLayer.getNum() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE from PossibilityStorage p where p.level.num='"+ levelLayer.getNum() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE from CompositionLevelSequence cmp where cmp.level.num='"+ levelLayer.getNum() +"'").executeUpdate();
        remove(levelLayer);
    }
    
}
