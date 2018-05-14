package controller;

import bean.Statut;
import service.StatutFacade;

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

@Named("statutController")
@SessionScoped
public class StatutController implements Serializable {

    @EJB
    private service.StatutFacade ejbFacade;
    private List<Statut> items = null;
    private Statut selected;

    public StatutController() {
    }
    
    public String redirect(){
        return "/statut/List?faces-redirect=true";
    }

    public Statut getSelected() {
        if (selected == null) {
            selected = new Statut();
        }
        return selected;
    }

    public void setSelected(Statut selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StatutFacade getFacade() {
        return ejbFacade;
    }

    public Statut prepareCreate() {
        selected = new Statut();
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

    public List<Statut> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Statut getStatut(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Statut> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Statut> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Statut.class)
    public static class StatutControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StatutController controller = (StatutController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "statutController");
            return controller.getStatut(getKey(value));
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
            if (object instanceof Statut) {
                Statut o = (Statut) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Statut.class.getName()});
                return null;
            }
        }

    }

}
