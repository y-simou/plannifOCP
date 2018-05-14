package controller;

import bean.Panel;
import service.PanelFacade;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("panelController")
@SessionScoped
public class PanelController implements Serializable {

    @EJB
    private service.PanelFacade ejbFacade;
    private List<Panel> items = null;
    private Panel selected;

    public PanelController() {
    }
    
    public String redirect(){
        return "/panel//List?faces-redirect=true";
    }

    public Panel getSelected() {
        if (selected ==null) {
            selected = new Panel();
        }
        return selected;
    }

    public void setSelected(Panel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PanelFacade getFacade() {
        return ejbFacade;
    }

    public Panel prepareCreate() {
        selected = new Panel();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(selected);
    }

    public void update() {
        ejbFacade.edit(selected);
    }

    public void delete() {
        ejbFacade.remove(selected);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Panel> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }


    public Panel getPanel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Panel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Panel> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Panel.class)
    public static class PanelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PanelController controller = (PanelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "panelController");
            return controller.getPanel(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Panel) {
                Panel o = (Panel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Panel.class.getName()});
                return null;
            }
        }

    }

}
