/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Axe;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class AxeFacade extends AbstractFacade<Axe> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AxeFacade() {
        super(Axe.class);
    }
    
    public void delete(Long axe){
        getEntityManager().createQuery("DELETE FROM Ville v where v.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM ChemicalComponentLayer ccl where ccl.chemicalComponent.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM ChemicalComponent cc where cc.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Site s where s.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Movement m where m.arrive.machine.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Storage s where s.machine.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Treatment t where t.machine.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From AvailabilityMachine av where av.machine.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Accessibility ac where ac.machine.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Machine ma where ma.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From RH rh where rh.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Storage s where s.stock.site.axe.id='"+ axe +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Stock st where st.site.axe.id='"+ axe +"'").executeUpdate();
        remove(find(axe));
        
    }
    
}
