/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Stock;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class StockFacade extends AbstractFacade<Stock> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StockFacade() {
        super(Stock.class);
    }
    
    public void delete(Stock stock){
        getEntityManager().createQuery("DELETE FROM Storage s where s.stock.id='"+ stock.getId() +"'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM PossibilityStorage p where p.stock.id='"+ stock.getId() +"'").executeUpdate();
        remove(stock);
    }
    
    public List<Stock> findAllAsc(){
        return getEntityManager().createQuery("select s from Stock s ORDER BY s.site.axe.id, s.site.id, s.nom").getResultList();
    }
}
