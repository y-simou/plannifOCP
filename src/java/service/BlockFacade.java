/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Block;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class BlockFacade extends AbstractFacade<Block> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BlockFacade() {
        super(Block.class);
    }
    
     public void delete(Long block){
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.id='"+ block +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.id='"+ block +"'").executeUpdate();
        remove(find(block));
    }
    
}
