/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.ChemicalComponent;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class ChemicalComponentFacade extends AbstractFacade<ChemicalComponent> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChemicalComponentFacade() {
        super(ChemicalComponent.class);
    }
    
    public ChemicalComponent findByNom(String nom){
        List<ChemicalComponent> ccs = getEntityManager().createQuery("SELECT c FROM ChemicalComponent c where c.nom='"+ nom +"'").getResultList();
        if (ccs.isEmpty()) {
            return null;
        }else{
            return ccs.get(0);
        }
    }
    
}
