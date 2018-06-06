/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ChemicalComponent;
import bean.ChemicalComponentLayer;
import bean.Layer;
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

    public void createComponantLayer(Layer layer, ChemicalComponent chemicalComponent,BigDecimal teneur) {
      List<ChemicalComponentLayer> ccls =  getEntityManager().createQuery("SELECT ccl FROM ChemicalComponentLayer ccl where ccl.chemicalComponent.id='"+ chemicalComponent.getId() +"' And ccl.layer.id='"+ layer.getId() +"'").getResultList();
        if (ccls.isEmpty()) {
            create(new ChemicalComponentLayer(layer, chemicalComponent, teneur));
        }else{
            ChemicalComponentLayer ccl = ccls.get(0);
            ccl.setTeneur(teneur);
            edit(ccl);
        }
    }

}
