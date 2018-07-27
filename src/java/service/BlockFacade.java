/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Block;
import bean.LevelLayer;
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
public class BlockFacade extends AbstractFacade<Block> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @EJB
    private LevelLayerFacade levelLayerFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BlockFacade() {
        super(Block.class);
    }

    public void delete(Long block) {
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.id='" + block + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.id='" + block + "'").executeUpdate();
        remove(find(block));
    }

    public void create(Block block, Long id) {
        LevelLayer ll = null;
        if (id != null) {
            ll = levelLayerFacade.find(id);
        }
        block.setLevel(ll);
        super.create(block);
    }

    public void update(Block block, Long id) {
        if (id != null) {
            LevelLayer ll = levelLayerFacade.find(id);
            block.setLevel(ll);
        }

        edit(block);
    }

    public List<Block> findByLevel(Long level) {
        return getEntityManager().createQuery("SELECT b FROM Block b where b.level.id='" + level + "'").getResultList();
    }

    public List<Block> findAllAsc() {
        return getEntityManager().createQuery("SELECT b FROM Block b ORDER BY b.level.parcel.trench.panel.id, b.level.parcel.trench.id,b.level.parcel.id,b.level.sequenceNiveau,b.id").getResultList();
    }

}
