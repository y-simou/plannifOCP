package controller;

import bean.PossibilityStorage;
import service.PossibilityStorageFacade;

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

@Named("possibilityStorageController")
@SessionScoped
public class PossibilityStorageController implements Serializable {

    @EJB
    private service.PossibilityStorageFacade ejbFacade;
    private List<PossibilityStorage> items = null;
    private PossibilityStorage selected;

    public PossibilityStorageController() {
    }

    public String redirect(){
        return "/possibilityStorage/List?faces-redirect=true";
    }
    
    public PossibilityStorage getSelected() {
        if (selected == null) {
            selected = new PossibilityStorage();
        }
        return selected;
    }

    public void setSelected(PossibilityStorage selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PossibilityStorageFacade getFacade() {
        return ejbFacade;
    }

    public PossibilityStorage prepareCreate() {
        selected = new PossibilityStorage();
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

    public List<PossibilityStorage> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }


    public PossibilityStorage getPossibilityStorage(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PossibilityStorage> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PossibilityStorage> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PossibilityStorage.class)
    public static class PossibilityStorageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PossibilityStorageController controller = (PossibilityStorageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "possibilityStorageController");
            return controller.getPossibilityStorage(getKey(value));
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
            if (object instanceof PossibilityStorage) {
                PossibilityStorage o = (PossibilityStorage) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PossibilityStorage.class.getName()});
                return null;
            }
        }

    }

}
