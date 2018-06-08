/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Panel;
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

    public Trench findByNomAndPanel(String nom, Long panel) {
        List<Trench> ts = getEntityManager().createQuery("SELECT t FROM Trench t where t.nom='" + nom + "' AND t.panel.id='" + panel + "'").getResultList();
        if (ts.isEmpty()) {
            return null;
        } else {
            return ts.get(0);
        }

    }

    public Trench createNull(String nom, Panel panel) {
        if (nom != null && nom.length() > 0 && nom.charAt(nom.length() - 1) == '0' && nom.charAt(nom.length() - 2) == '.') {
//                if (nom != null && nom.length() > 0 && nom.substring(nom.length() - 1, nom.length() - 2).equals(".0")) {
            nom = nom.substring(0, nom.length() - 2);
        }

        Trench trench = findByNomAndPanel(nom, panel.getId());
        if (trench == null) {
            trench = new Trench(generateId("Trench", "id"),nom, panel);
            create(trench);
            Readxl.trenchConteur++;

        }
        
        return trench;
    }

}
