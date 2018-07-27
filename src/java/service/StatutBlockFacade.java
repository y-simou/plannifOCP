/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Block;
import bean.StatutBlock;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class StatutBlockFacade extends AbstractFacade<StatutBlock> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;
    @EJB
    private BlockFacade blockFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StatutBlockFacade() {
        super(StatutBlock.class);
    }
    
    public List<StatutBlock> findAllAsc(){
        return getEntityManager().createQuery("select s from StatutBlock s where 1=1 ORDER BY s.dateChangement").getResultList();
    }
    
    public void create(StatutBlock statutBlock, Long id) {
        Block b = null;
        if (id != null) {
            b = blockFacade.find(id);
        }
        statutBlock.setBlock(b);
        super.create(statutBlock);
    }

    public void update(StatutBlock statutBlock, Long id) {
        if (id != null) {
           Block  b = blockFacade.find(id);
            statutBlock.setBlock(b);
        }
        edit(statutBlock);
    }
    
}
