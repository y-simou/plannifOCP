package controller;

import bean.StatutParcel;
import service.StatutParcelFacade;

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

@Named("statutParcelController")
@SessionScoped
public class StatutParcelController implements Serializable {

    @EJB
    private service.StatutParcelFacade ejbFacade;
    private List<StatutParcel> items = null;
    private StatutParcel selected;

    public String redirect() {
        return "/statutParcel/List?faces-redirect=true";
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

    public void delete(StatutParcel statutParcel) {
        ejbFacade.remove(statutParcel);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public StatutParcelController() {
    }

    public StatutParcel getSelected() {
        if (selected==null) {
            selected = new StatutParcel();
        }
        return selected;
    }

    public void setSelected(StatutParcel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StatutParcelFacade getFacade() {
        return ejbFacade;
    }

    public StatutParcel prepareCreate() {
        selected = new StatutParcel();
        initializeEmbeddableKey();
        return selected;
    }

    public List<StatutParcel> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public StatutParcel getStatutParcel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<StatutParcel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<StatutParcel> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = StatutParcel.class)
    public static class StatutParcelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StatutParcelController controller = (StatutParcelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "statutParcelController");
            return controller.getStatutParcel(getKey(value));
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
            if (object instanceof StatutParcel) {
                StatutParcel o = (StatutParcel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), StatutParcel.class.getName()});
                return null;
            }
        }

    }

}
