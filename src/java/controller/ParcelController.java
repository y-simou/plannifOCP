package controller;

import bean.Parcel;
import service.ParcelFacade;

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

@Named("parcelController")
@SessionScoped
public class ParcelController implements Serializable {

    @EJB
    private service.ParcelFacade ejbFacade;
    private List<Parcel> items = null;
    private Parcel selected;

    public ParcelController() {
    }
    
    public String redirect(){
        return "/parcel/List?faces-redirect=true";
    }

    public Parcel getSelected() {
        if (selected ==null) {
            selected = new Parcel();
        }
        return selected;
    }

    public void setSelected(Parcel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ParcelFacade getFacade() {
        return ejbFacade;
    }

    public Parcel prepareCreate() {
        selected = new Parcel();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(selected);
    }

    public void update() {
        ejbFacade.edit(selected);
    }

    public void delete(Parcel parcel) {
        ejbFacade.delete(parcel);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Parcel> getItems() {
        if (items == null) {
            items = ejbFacade.findAllOrder();
        }
        return items;
    }


    public Parcel getParcel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Parcel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Parcel> getItemsAvailableSelectOne() {
        return getFacade().findAllOrder();
    }

    @FacesConverter(forClass = Parcel.class)
    public static class ParcelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ParcelController controller = (ParcelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "parcelController");
            return controller.getParcel(getKey(value));
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
            if (object instanceof Parcel) {
                Parcel o = (Parcel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Parcel.class.getName()});
                return null;
            }
        }

    }

}
