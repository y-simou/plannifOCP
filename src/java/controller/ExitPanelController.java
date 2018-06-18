/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.ExitPanel;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Yassine.SIMOU
 */
@Named(value = "exitPanelController")
@SessionScoped
public class ExitPanelController implements Serializable {

    /**
     * Creates a new instance of ExitPanelController
     */
    public ExitPanelController() {
    }
    
    @EJB
    private service.ExitPanelFacade ejbFacade;
    private List<ExitPanel> items = null;
    private ExitPanel selected;

    public String redirect() {
        return "/exitPanel/List?faces-redirect=true";
    }
    public void create() {
        ejbFacade.create(selected);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void update() {
        ejbFacade.edit(selected);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void delete(ExitPanel exitPanel) {
        ejbFacade.remove(exitPanel);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<ExitPanel> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    public void setItems(List<ExitPanel> items) {
        this.items = items;
    }

    public ExitPanel getSelected() {
         if (selected == null) {
            selected = new ExitPanel();
        }
        return selected;
    }

    public void setSelected(ExitPanel selected) {
        this.selected = selected;
    }
    
    public List<ExitPanel> getItemsAvailableSelectMany() {
        return ejbFacade.findAll();
    }

    public List<ExitPanel> getItemsAvailableSelectOne() {
        return ejbFacade.findAll();
    }

}
