/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.LevelLayer;
import bean.PossibilityStorage;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class PossibilityStorageFacade extends AbstractFacade<PossibilityStorage> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @EJB
    private LevelLayerFacade levelLayerFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PossibilityStorageFacade() {
        super(PossibilityStorage.class);
    }

    public void create(PossibilityStorage possibilityStorage, Long id) {
        LevelLayer ll = null;
        if (id != null) {
            ll = levelLayerFacade.find(id);
        }
        possibilityStorage.setLevel(ll);
        super.create(possibilityStorage);
    }

    public void update(PossibilityStorage possibilityStorage, Long id) {
        if (id != null) {
           LevelLayer ll = levelLayerFacade.find(id);
        possibilityStorage.setLevel(ll);
        }
        edit(possibilityStorage);
    }

}
