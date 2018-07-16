/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Panel;
import java.util.List;
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

    public void delete(Panel panel) {
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.level.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.level.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Storage st where st.block.level.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Block b where b.level.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM ChemicalComponentLayer ccl where ccl.layer.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM CompositionLevelSequence cls where cls.level.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM LevelLayer ll where ll.parcel.trench.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Parcel p where p.trench.panel.id='" + panel.getId() + "' OR p.subPanel.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Trench t where t.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM CCL c where c.subPanel.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM SubPanel sp where sp.panel.id='" + panel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM ExitPanel ep where ep.panel.id='" + panel.getId() + "'").executeUpdate();
        remove(panel);
    }

    public Panel findByNom(String nom) {
        List<Panel> ps = getEntityManager().createQuery("SELECT p FROM Panel p where p.nom=\"" + nom + "\"").getResultList();
        if (ps.isEmpty()) {
            return null;
        } else {
            return ps.get(0);
        }

    }

    public Panel createNull(String nom) {
        
        if (nom.equals("") && nom.length() > 0 && nom.charAt(nom.length() - 1) == '0' && nom.charAt(nom.length() - 2) == '.') {
            nom = nom.substring(0, nom.length() - 2);
        }

        Panel panel = findByNom(nom);
        if (panel == null) {
            panel = new Panel(generateId("Panel", "id"), nom);
            create(panel);
            Readxl.panelConteur++;
        }
        return panel;
    }

}
