/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ChemicalComponent;
import bean.ChemicalComponentLayer;
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
public class ChemicalComponentLayerFacade extends AbstractFacade<ChemicalComponentLayer> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;
    @EJB
    private LevelLayerFacade levelLayerFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChemicalComponentLayerFacade() {
        super(ChemicalComponentLayer.class);
    }

    public void createComponantLevel(LevelLayer layer, ChemicalComponent chemicalComponent, Double teneur) {
        create(new ChemicalComponentLayer(layer, chemicalComponent, teneur));
    }

    public List<ChemicalComponentLayer> findAllAsc() {
        return getEntityManager().createQuery("SELECT c FROM ChemicalComponentLayer c ORDER BY c.layer.parcel.trench.panel.id, c.layer.parcel.trench.id,c.layer.parcel.id,c.layer.sequenceNiveau,c.chemicalComponent.id").getResultList();
    }

    public void create(ChemicalComponentLayer chemicalComponentLayer, Long id) {
        LevelLayer ll = null;
        if (id != null) {
            ll = levelLayerFacade.find(id);
        }
        chemicalComponentLayer.setLevelLayer(ll);
        super.create(chemicalComponentLayer);
    }

    public void update(ChemicalComponentLayer chemicalComponentLayer, Long id) {
        if (id != null) {
            LevelLayer ll = levelLayerFacade.find(id);
            chemicalComponentLayer.setLevelLayer(ll);
        }
        super.edit(chemicalComponentLayer);
    }

}
