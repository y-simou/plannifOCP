/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.LevelLayer;
import bean.Parcel;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class LevelLayerFacade extends AbstractFacade<LevelLayer> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    @EJB
    private ParcelFacade parcelFacade = new ParcelFacade();

    public LevelLayerFacade() {
        super(LevelLayer.class);
    }

    public void delete(LevelLayer levelLayer) {
        getEntityManager().createQuery("DELETE from ChemicalComponentLayer ch where ch.layer.level.id'" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Layer l where l.level.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Storage st where st.block.level.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Accessibility a where a.levelLayer.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from StatutBlock s where s.block.level.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Treatment t where t.block.level.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from Block b where b.level.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from PossibilityStorage p where p.level.id='" + levelLayer.getId() + "'").executeUpdate();
        getEntityManager().createQuery("DELETE from CompositionLevelSequence cmp where cmp.level.id='" + levelLayer.getId() + "'").executeUpdate();
        remove(levelLayer);
    }

    public LevelLayer createNull(String nom, Parcel parcel, Double puissance, Double volume, Double thc, Double taux, Double surface) {
        LevelLayer level;
        List<LevelLayer> lls = getEntityManager().createQuery("SELECT l FROM LevelLayer l where l.nom='" + nom + "' and l.parcel.id='" + parcel.getId() + "'").getResultList();
        if (lls.isEmpty()) {
            level = new LevelLayer(generateSequense(parcel), nom, puissance, new BigDecimal(surface), new BigDecimal(volume), new BigDecimal(thc), new BigDecimal(taux), true, parcel);
            create(level);
            Double epaisseur = parcel.getEpaisseur();
            if (epaisseur == null) {
                epaisseur = new Double(0);
            }
            parcel.setEpaisseur(epaisseur + puissance);
            parcelFacade.edit(parcel);
            Readxl.layerConteur++;
        } else {
            level = lls.get(0);
        }
        return level;
    }

    public void createLevelNull(String nom, Double puissance, Double volume, Double surface, Parcel parcel) {
        LevelLayer level;
        List<LevelLayer> lls = getEntityManager().createQuery("SELECT l FROM LevelLayer l where l.nom='" + nom + "' and l.parcel.id='" + parcel.getId() + "'").getResultList();
        if (lls.isEmpty()) {

            level = new LevelLayer(generateSequense(parcel), nom, puissance, new BigDecimal(surface), new BigDecimal(volume), null, null, false, parcel);
            create(level);
            Double epaisseur = parcel.getEpaisseur();
            if (epaisseur == null) {
                epaisseur = new Double(0);
            }
            parcel.setEpaisseur(epaisseur + puissance);
            parcelFacade.edit(parcel);
            Readxl.layerConteur++;
        }
    }

    public int generateSequense(Parcel parcel) {
        int s = (int) getEntityManager().createQuery("SELECT max(l.sequenceNiveau) from LevelLayer l where l.parcel.id='" + parcel.getId() + "'").getSingleResult();
        return s == 0 ? 1 : s + 1;
    }

    public List<String> findNomByParcel(Long parcel) {
        return getEntityManager().createQuery("SELECT l.nom from LevelLayer l where l.parcel.id='" + parcel + "' ORDER BY l.sequenceNiveau").getResultList();
    }
    
    public List<String> findSubPanel(Long sp) {
        Parcel p = (Parcel) getEntityManager().createQuery("SELECT p from Parcel p where p.subPanel.id='" + sp + "'").setMaxResults(1).getSingleResult();
        return findNomByParcel(p.getId());
    }

    public List<Double> findPuissanceByParcel(Long parcel) {
        return getEntityManager().createQuery("SELECT l.puissance from LevelLayer l where l.parcel.id='" + parcel + "' ORDER BY l.sequenceNiveau").getResultList();
    }

    public List<LevelLayer> findAllOrdred() {
        return getEntityManager().createQuery("SELECT l from LevelLayer l ORDER BY l.parcel.id,l.sequenceNiveau").getResultList();
    }
}
