/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.PanelSubPanel;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class PanelSubPanelFacade extends AbstractFacade<PanelSubPanel> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PanelSubPanelFacade() {
        super(PanelSubPanel.class);
    }

    @Override
    public void create(PanelSubPanel panelSubPanel) {
        List<PanelSubPanel> panelSubPanels = getEntityManager().createQuery("SELECT p FROM PanelSubPanel p where p.panel.id='" + panelSubPanel.getPanel().getId() + "' and p.subpanel.id='" + panelSubPanel.getSubpanel().getId() + "'").getResultList();
        if (panelSubPanels.isEmpty()) {
            super.create(new PanelSubPanel(panelSubPanel.getPanel(), panelSubPanel.getSubpanel()));
        }
    }
}
