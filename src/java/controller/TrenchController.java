package controller;

import bean.Trench;
import service.TrenchFacade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("trenchController")
@SessionScoped
public class TrenchController implements Serializable {

    @EJB
    private service.TrenchFacade ejbFacade;
    private List<Trench> items = null;
    private Trench selected;

    public boolean filterByDate(Object value, Object filter, Locale locale) {

        if (filter == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return !((Date) value).before((Date) filter);
    }

    public TrenchController() {
    }

    public String redirect() {
        return "/trench/List?faces-redirect=true";
    }

    public Trench getSelected() {
        if (selected == null) {
            selected = new Trench();
        }
        return selected;
    }

    public void setSelected(Trench selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TrenchFacade getFacade() {
        return ejbFacade;
    }

    public Trench prepareCreate() {
        selected = new Trench();
        initializeEmbeddableKey();
        return selected;
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

    public void delete(Trench trench) {
        ejbFacade.delete(trench.getId());
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Trench> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Trench getTrench(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Trench> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Trench> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Trench.class)
    public static class TrenchControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TrenchController controller = (TrenchController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "trenchController");
            return controller.getTrench(getKey(value));
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
            if (object instanceof Trench) {
                Trench o = (Trench) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Trench.class.getName()});
                return null;
            }
        }

    }

}
