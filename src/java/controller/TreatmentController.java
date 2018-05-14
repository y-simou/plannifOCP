package controller;

import bean.Treatment;
import service.TreatmentFacade;

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

@Named("treatmentController")
@SessionScoped
public class TreatmentController implements Serializable {

    @EJB
    private service.TreatmentFacade ejbFacade;
    private List<Treatment> items = null;
    private Treatment selected;

    public TreatmentController() {
    }

    public Treatment getSelected() {
        if (selected == null ) {
            selected = new Treatment();
        }
        return selected;
    }

    public void setSelected(Treatment selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TreatmentFacade getFacade() {
        return ejbFacade;
    }

    public Treatment prepareCreate() {
        selected = new Treatment();
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

    public List<Treatment> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Treatment getTreatment(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Treatment> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Treatment> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Treatment.class)
    public static class TreatmentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TreatmentController controller = (TreatmentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "treatmentController");
            return controller.getTreatment(getKey(value));
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
            if (object instanceof Treatment) {
                Treatment o = (Treatment) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Treatment.class.getName()});
                return null;
            }
        }

    }

}
