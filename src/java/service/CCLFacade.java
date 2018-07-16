/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.CCL;
import bean.SubPanel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class CCLFacade extends AbstractFacade<CCL> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CCLFacade() {
        super(CCL.class);
    }
    
    public CCL createGroupe(String nom,SubPanel subPanel){
        List<CCL> ccls =getEntityManager().createQuery("SELECT c FROM CCL c where c.nom='" + nom + "' and c.subPanel.id='" + subPanel.getId() + "'").getResultList();
        CCL ccl;
        if (ccls.isEmpty()) {
            ccl = new CCL(generateId("CCL", "id"), nom, subPanel);
            create(ccl);
        }else{
            ccl = ccls.get(0);
        }
        return ccl;
    }
    
}
