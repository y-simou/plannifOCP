package controller;

import bean.Storage;
import service.StorageFacade;

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

@Named("storageController")
@SessionScoped
public class StorageController implements Serializable {

    @EJB
    private service.StorageFacade ejbFacade;
    private List<Storage> items = null;
    private Storage selected;

    public StorageController() {
    }

    public String redirect() {
        return "/storage/List?faces-redirect=true";
    }

    public Storage getSelected() {
        if (selected == null) {
            selected = new Storage();
        }
        return selected;
    }

    public void setSelected(Storage selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StorageFacade getFacade() {
        return ejbFacade;
    }

    public Storage prepareCreate() {
        selected = new Storage();
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

    public void delete(Storage storage) {
        ejbFacade.remove(storage);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Storage> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Storage getStorage(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Storage> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Storage> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Storage.class)
    public static class StorageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StorageController controller = (StorageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "storageController");
            return controller.getStorage(getKey(value));
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
            if (object instanceof Storage) {
                Storage o = (Storage) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Storage.class.getName()});
                return null;
            }
        }

    }

}
