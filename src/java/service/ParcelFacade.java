/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Parcel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class ParcelFacade extends AbstractFacade<Parcel> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParcelFacade() {
        super(Parcel.class);
    }
    
    public void delete(Parcel parcel){
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.parcel.trench.panel.id='"+ parcel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.parcel.trench.panel.id='"+ parcel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Storage st where st.block.parcel.trench.panel.id='"+ parcel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Block b where b.parcel.trench.panel.id='"+ parcel.getId() +"'").executeUpdate();
        remove(parcel);
    }
    
}
