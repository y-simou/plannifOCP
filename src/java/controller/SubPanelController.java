package controller;

import bean.SubPanel;
import service.SubPanelFacade;

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

@Named("subPanelController")
@SessionScoped
public class SubPanelController implements Serializable {

    @EJB
    private service.SubPanelFacade ejbFacade;
    private List<SubPanel> items = null;
    private SubPanel selected;

    public SubPanelController() {
    }
    
    public String redirect(){
        return "/subPanel/List?faces-redirect=true";
    }

    public SubPanel getSelected() {
        if (selected == null ) {
            selected = new SubPanel();
        }
        return selected;
    }

    public void setSelected(SubPanel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SubPanelFacade getFacade() {
        return ejbFacade;
    }

    public SubPanel prepareCreate() {
        selected = new SubPanel();
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

    public List<SubPanel> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }


    public SubPanel getSubPanel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<SubPanel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<SubPanel> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = SubPanel.class)
    public static class SubPanelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SubPanelController controller = (SubPanelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "subPanelController");
            return controller.getSubPanel(getKey(value));
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
            if (object instanceof SubPanel) {
                SubPanel o = (SubPanel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), SubPanel.class.getName()});
                return null;
            }
        }

    }

}
