package controller;

import bean.AvailabilityRh;
import service.AvailabilityRhFacade;

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

@Named("availabilityRhController")
@SessionScoped
public class AvailabilityRhController implements Serializable {

    @EJB
    private service.AvailabilityRhFacade ejbFacade;
    private List<AvailabilityRh> items = null;
    private AvailabilityRh selected;

    public String redirect() {
        return "/availabilityRh/List?faces-redirect=true";
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

    public void delete(AvailabilityRh availabilityRh) {
        ejbFacade.remove(availabilityRh);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public AvailabilityRhController() {
    }

    public AvailabilityRh getSelected() {
        if (selected==null) {
            selected = new AvailabilityRh();
        }
        return selected;
    }

    public void setSelected(AvailabilityRh selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvailabilityRhFacade getFacade() {
        return ejbFacade;
    }

    public AvailabilityRh prepareCreate() {
        selected = new AvailabilityRh();
        initializeEmbeddableKey();
        return selected;
    }

    public List<AvailabilityRh> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public AvailabilityRh getAvailabilityRh(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AvailabilityRh> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AvailabilityRh> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AvailabilityRh.class)
    public static class AvailabilityRhControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvailabilityRhController controller = (AvailabilityRhController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "availabilityRhController");
            return controller.getAvailabilityRh(getKey(value));
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
            if (object instanceof AvailabilityRh) {
                AvailabilityRh o = (AvailabilityRh) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvailabilityRh.class.getName()});
                return null;
            }
        }

    }

}
