/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.PositionMachine;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class PositionMachineFacade extends AbstractFacade<PositionMachine> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PositionMachineFacade() {
        super(PositionMachine.class);
    }

    public List<PositionMachine> findByMachine(Long id) {
        return getEntityManager().createQuery("SELECT p From PositionMachine p where p.machine.id='" + id + "'").getResultList();
    }

}
