package controller;

import bean.Axe;
import service.AxeFacade;

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

@Named("axeController")
@SessionScoped
public class AxeController implements Serializable {

    @EJB
    private service.AxeFacade ejbFacade;
    private List<Axe> items = null;
    private Axe selected;

    public String redirect() {
        return "/axe/List?faces-redirect=true";
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

    public void delete(Axe axe) {
        ejbFacade.remove(axe);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public AxeController() {
    }

    public Axe getSelected() {
        if (selected == null) {
            selected = new Axe();
        }
        return selected;
    }

    public void setSelected(Axe selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AxeFacade getFacade() {
        return ejbFacade;
    }

    public Axe prepareCreate() {
        selected = new Axe();
        initializeEmbeddableKey();
        return selected;
    }

    public List<Axe> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Axe getAxe(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Axe> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Axe> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Axe.class)
    public static class AxeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AxeController controller = (AxeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "axeController");
            return controller.getAxe(getKey(value));
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
            if (object instanceof Axe) {
                Axe o = (Axe) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Axe.class.getName()});
                return null;
            }
        }

    }

}
