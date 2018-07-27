/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Accessibility;
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
public class AccessibilityFacade extends AbstractFacade<Accessibility> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @EJB
    private LevelLayerFacade levelLayerFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccessibilityFacade() {
        super(Accessibility.class);
    }

    public void create(Accessibility accessibility, Long id) {
        LevelLayer ll = null;
        if (id != null) {
            ll = levelLayerFacade.find(id);
        }
        accessibility.setLevelLayer(ll);
        super.create(accessibility);
    }

    public void update(Accessibility accessibility, Long id) {
        if (id != null) {
            LevelLayer ll = levelLayerFacade.find(id);
            accessibility.setLevelLayer(ll);
        }
        edit(accessibility);
    }

    public List<Accessibility> findAllAsc() {
        return getEntityManager().createQuery("SELECT a from Accessibility a ORDER BY a.machine.id, a.levelLayer.parcel.trench.panel.id, a.levelLayer.parcel.trench.id, a.levelLayer.parcel.id, a.levelLayer.id").getResultList();
    }

}
