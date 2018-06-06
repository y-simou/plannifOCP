package service;

import bean.Annee;
import service.util.JsfUtil;
import service.util.JsfUtil.PersistAction;
import controller.AnneeFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("anneeController")
@SessionScoped
public class AnneeController implements Serializable {

    @EJB
    private controller.AnneeFacade ejbFacade;
    private List<Annee> items = null;
    private Annee selected;

    public AnneeController() {
    }
    
    public String redirect(){
        return "/annee/List?faces-redirect=true";
    }

    public Annee getSelected() {
        if (selected == null) {
            selected = new Annee();
        }
        return selected;
    }

    public void setSelected(Annee selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AnneeFacade getFacade() {
        return ejbFacade;
    }

    public Annee prepareCreate() {
        selected = new Annee();
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

    public void delete(Annee annee) {
        ejbFacade.remove(annee);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Annee> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Annee getAnnee(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Annee> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Annee> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Annee.class)
    public static class AnneeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AnneeController controller = (AnneeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "anneeController");
            return controller.getAnnee(getKey(value));
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
            if (object instanceof Annee) {
                Annee o = (Annee) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Annee.class.getName()});
                return null;
            }
        }

    }

}
