/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Parcel;
import bean.SubPanel;
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

    public void delete(Parcel parcel) {
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.level.parcel.id='" + parcel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.level.parcel.id='" + parcel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Storage st where st.block.level.parcel.id='" + parcel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Block b where b.level.parcel.id='" + parcel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM ChemicalComponentLayer ccl where ccl.layer.parcel.id='" + parcel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM CompositionLevelSequence cls where cls.level.parcel.id='" + parcel.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM LevelLayer ll where ll.parcel.id='" + parcel.getId() + "'").executeUpdate();
        remove(parcel);
    }

    public Parcel findByNomAndTrench(String nom, Long trench) {
        List<Parcel> ps = getEntityManager().createQuery("SELECT p FROM Parcel p where p.nom='" + nom + "' AND p.trench.id='" + trench + "'").getResultList();
        if (ps.isEmpty()) {
            return null;
        } else {
            return ps.get(0);
        }

    }
    
    public List<Parcel> findByTrench(Long trench) {
        return getEntityManager().createQuery("SELECT p FROM Parcel p where p.trench.id='" + trench + "'").getResultList();
    }

    public Parcel createNull(String nom, Trench trench) {

        if (nom != null && nom.length() > 0 && nom.charAt(nom.length() - 1) == '0' && nom.charAt(nom.length() - 2) == '.') {
            nom = nom.substring(0, nom.length() - 2);
        }
        Parcel parcel = findByNomAndTrench(nom, trench.getId());
        if (parcel == null) {
            parcel = new Parcel(generateId("Parcel", "id"), nom, trench);
            create(parcel);
            Readxl.parcelConteur++;

        }
        return parcel;
    }

    public List<Parcel> findByPanel(Long panel) {
        return getEntityManager().createQuery("SELECT p From Parcel p where p.trench.panel.id='" + panel + "'").getResultList();
    }

    public List<Parcel> findBySubPanel(Long subPanel) {
        return getEntityManager().createQuery("SELECT p From Parcel p where p.subPanel.id='" + subPanel + "'").getResultList();
    }

    public List<Parcel> findByPanelAsc(Long panel) {
        return getEntityManager().createQuery("SELECT p From Parcel p where p.trench.panel.id='" + panel + "' ORDER BY p.subPanel.id, p.ccl.id").getResultList();
    }
    
    public List<Parcel> findAllOrder() {
        return getEntityManager().createQuery("SELECT p From Parcel p ORDER BY p.trench.panel.id, p.trench.id, p.subPanel.id, p.ccl.id").getResultList();
    }
}
