/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SubPanel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class SubPanelFacade extends AbstractFacade<SubPanel> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubPanelFacade() {
        super(SubPanel.class);
    }
    
     public void delete(Long spanel){
        getEntityManager().createQuery("DELETE FROM Parcel pa where pa.subPanel.id'"+ spanel +"'").executeUpdate();
        remove(find(spanel));
    }
    
}
