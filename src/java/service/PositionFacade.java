/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Position;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class PositionFacade extends AbstractFacade<Position> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PositionFacade() {
        super(Position.class);
    }

    public void delete(Position position) {
        getEntityManager().createQuery("DELETE FROM Movement m where m.arrive.id='" + position.getId() + "' OR m.depart.id='" + position.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM PositionMachine pm where pm.position.id='" + position.getId() + "'").executeUpdate();
        remove(position);
    }

    public void creer(Position position) {
        position.setId(generateId("Position", "id"));
        create(position);
    }

    public List<Position> findAllDesc() {
        return getEntityManager().createQuery("SELECT p FROM Position p where 1=1 Order by p.x,p.y,p.nom").getResultList();
    }

}
