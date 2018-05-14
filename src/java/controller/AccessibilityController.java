package controller;

import bean.Accessibility;
import service.AccessibilityFacade;

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

@Named("accessibilityController")
@SessionScoped
public class AccessibilityController implements Serializable {

    @EJB
    private AccessibilityFacade ejbFacade;
    private List<Accessibility> items = null;
    private Accessibility selected;

    public AccessibilityController() {
    }
    
    public String redirect(){
        return "/accessibility/List?faces-redirect=true";
    }

    public Accessibility getSelected() {
        if (selected == null) {
            selected = new Accessibility();
        }
        return selected;
    }

    public void setSelected(Accessibility selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AccessibilityFacade getFacade() {
        return ejbFacade;
    }

    public Accessibility prepareCreate() {
        selected = new Accessibility();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(selected);
        items = ejbFacade.findAll();
    }

    public void modifie() {
        ejbFacade.edit(selected);
    }

    public void delete(Accessibility access) {
        ejbFacade.remove(access);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Accessibility> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    public Accessibility getAccessibility(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Accessibility> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Accessibility> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Accessibility.class)
    public static class AccessibilityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AccessibilityController controller = (AccessibilityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "accessibilityController");
            return controller.getAccessibility(getKey(value));
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
            if (object instanceof Accessibility) {
                Accessibility o = (Accessibility) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Accessibility.class.getName()});
                return null;
            }
        }

    }

}
