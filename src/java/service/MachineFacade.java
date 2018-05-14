/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Machine;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class MachineFacade extends AbstractFacade<Machine> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MachineFacade() {
        super(Machine.class);
    }
    
    public void delete(Machine machine){
        getEntityManager().createQuery("DELETE From Movement m where m.machine.id='"+ machine.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Storage s where s.machine.id='"+ machine.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Treatment t where t.machine.id='"+ machine.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From AvailabilityMachine av where av.machine.id='"+ machine.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE From Accessibility ac where ac.machine.id='"+ machine.getId() +"'").executeUpdate();
        remove(machine);
    }
}
