package controller;

import bean.Ville;
import service.VilleFacade;

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

@Named("villeController")
@SessionScoped
public class VilleController implements Serializable {

    @EJB
    private service.VilleFacade ejbFacade;
    private List<Ville> items = null;
    private Ville selected;

    public String redirect() {
        return "/ville/List?faces-redirect=true";
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

    public void delete(Ville ville) {
        ejbFacade.remove(ville);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public VilleController() {
    }

    public Ville getSelected() {
        if (selected==null) {
            selected = new Ville();
        }
        return selected;
    }

    public void setSelected(Ville selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VilleFacade getFacade() {
        return ejbFacade;
    }

    public Ville prepareCreate() {
        selected = new Ville();
        initializeEmbeddableKey();
        return selected;
    }

    public List<Ville> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Ville getVille(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Ville> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ville> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Ville.class)
    public static class VilleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VilleController controller = (VilleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "villeController");
            return controller.getVille(getKey(value));
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
            if (object instanceof Ville) {
                Ville o = (Ville) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Ville.class.getName()});
                return null;
            }
        }

    }

}
