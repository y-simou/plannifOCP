/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Panel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class PanelFacade extends AbstractFacade<Panel> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PanelFacade() {
        super(Panel.class);
    }
    
    public void delete(Panel panel){
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.parcel.trench.panel.id='"+ panel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.parcel.trench.panel.id='"+ panel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Storage st where st.block.parcel.trench.panel.id='"+ panel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Block b where b.parcel.trench.panel.id='"+ panel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Parcel p where p.trench.panel.id='"+ panel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Trench t where t.panel.id='"+ panel.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Parcel pa where pa.subPanel.panel.id='"+ panel.getId() +"'").executeUpdate();
        remove(panel);
    }
    
}
