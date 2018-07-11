/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ChemicalComponent;
import bean.ChemicalComponentLayer;
import bean.LevelLayer;
import java.math.BigDecimal;
import java.util.List;
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChemicalComponentLayerFacade() {
        super(ChemicalComponentLayer.class);
    }

    public void createComponantLevel(LevelLayer layer, ChemicalComponent chemicalComponent, BigDecimal teneur) {
        create(new ChemicalComponentLayer(layer, chemicalComponent, teneur));
    }

}
