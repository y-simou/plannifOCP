/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.PanelSubPanel;
import bean.Parcel;
import bean.SubPanel;
import java.util.ArrayList;
import java.util.Arrays;
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
        getEntityManager().createQuery("DELETE FROM ChemicalComponentLayer ccl where ccl.layer.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM CompositionLevelSequence cls where cls.level.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM LevelLayer ll where ll.parcel.subPanel.id='" + spanel + "'").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Parcel pa where pa.subPanel.id='" + spanel + "'").executeUpdate();
        remove(find(spanel));
    }
    @EJB
    private LevelLayerFacade levelLayerFacade;
    @EJB
    private PanelSubPanelFacade panelSubPanelFacade;

    public SubPanel createStructure(Parcel parcel) {
        int exist = 0, index = 0;
        List<List<String>> structures = new ArrayList<List<String>>();
        List<String> structure = levelLayerFacade.findNomByParcel(parcel.getId()), s, sp;
        List<SubPanel> subPanels = findAll();
        for (int i = 0; i < subPanels.size(); i++) {
            sp = new ArrayList();
            SubPanel subPanel = subPanels.get(i);
            sp = levelLayerFacade.findSubPanel(subPanel.getId());
            if (sp!=null) {
                structures.add(sp);
            }
        }
        Object[] s1 = {structure};
        for (int j = 0; j < structures.size(); j++) {
            s = structures.get(j);
            Object[] s2 = {s};
            if (Arrays.equals(s1, s2)) {
                index = j;
                exist = 1;
                break;
            }
        }
        SubPanel subPanel;
        if (exist == 1) {
            subPanel = subPanels.get(index);
            exist = 0;
        } else {
            Long id = generateId("SubPanel", "id");
            subPanel = new SubPanel(id, id + "");
            create(subPanel);
        }
        PanelSubPanel panelSubPanel = new PanelSubPanel(parcel.getTrench().getPanel(), subPanel);
        panelSubPanelFacade.create(panelSubPanel);
        return subPanel;
    }

}
