/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Trench;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class TrenchFacade extends AbstractFacade<Trench> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrenchFacade() {
        super(Trench.class);
    }
    
    public Trench findByNomAndPanel(String nom,Long panel){
        List<Trench> ts = getEntityManager().createQuery("SELECT t FROM Trench t where t.nom='"+ nom +"' AND t.panel.id='"+ panel +"'").getResultList();
        if(ts.isEmpty()){
            return null;
        }else{
            return ts.get(0);
        }
        
        
    }
    
}
