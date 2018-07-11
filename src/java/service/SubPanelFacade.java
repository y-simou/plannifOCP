/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Panel;
import bean.SubPanel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class SubPanelFacade extends AbstractFacade<SubPanel> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubPanelFacade() {
        super(SubPanel.class);
    }

    public void delete(Long spanel) {
        getEntityManager().createQuery("DELETE FROM StatutBlock sb where sb.block.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Treatment tr where tr.block.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Storage st where st.block.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Block b where b.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM ChemicalComponentLayer ccl where ccl.layer.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Layer l where l.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM CompositionLevelSequence cls where cls.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM LevelLayer ll where ll.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Parcel pa where pa.subPanel.id='" + spanel + "'").executeUpdate();
        remove(find(spanel));
    }

    public SubPanel createStructure(String nom, Panel panel) {
        List<SubPanel> subPanels = getEntityManager().createQuery("SELECT s FROM SubPanel s where s.nom='" + nom + "' and s.panel.id='" + panel.getId() + "'").getResultList();
        SubPanel subPanel;
        if (subPanels.isEmpty()) {
            subPanel = new SubPanel(generateId("SubPanel", "id"), nom, panel);
            create(subPanel);
        }else{
            subPanel = subPanels.get(0);
        }
        return subPanel;
    }

}
