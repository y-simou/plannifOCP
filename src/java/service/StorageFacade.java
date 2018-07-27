/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Block;
import bean.Storage;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class StorageFacade extends AbstractFacade<Storage> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;
    @EJB
    private BlockFacade blockFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StorageFacade() {
        super(Storage.class);
    }

    public void create(Storage storage, Long id) {
        Block b = null;
        if (id != null) {
            b = blockFacade.find(id);
        }
        storage.setBlock(b);
        super.create(storage);
    }

    public void update(Storage storage, Long id) {
        if (id != null) {
           Block  b = blockFacade.find(id);
            storage.setBlock(b);
        }
        edit(storage);
    }

}
