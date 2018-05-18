package controller;

import bean.RH;
import service.RHFacade;

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

@Named("rHController")
@SessionScoped
public class RHController implements Serializable {

    @EJB
    private service.RHFacade ejbFacade;
    private List<RH> items = null;
    private RH selected;
    
     public String redirect(){
        return "/rH/List?faces-redirect=true";
    }
    
     public void create() {
        ejbFacade.create(selected);
        items = ejbFacade.findAll();
    }

    public void update() {
        ejbFacade.edit(selected);
    }

    public void delete(RH rh) {
        ejbFacade.remove(rh);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public RHController() {
    }

    public RH getSelected() {
         if (selected==null) {
            selected = new RH();
        }
        return selected;
    }

    public void setSelected(RH selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RHFacade getFacade() {
        return ejbFacade;
    }

    public RH prepareCreate() {
        selected = new RH();
        initializeEmbeddableKey();
        return selected;
    }


    public List<RH> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }


    public RH getRH(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<RH> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<RH> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = RH.class)
    public static class RHControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RHController controller = (RHController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "rHController");
            return controller.getRH(getKey(value));
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
            if (object instanceof RH) {
                RH o = (RH) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), RH.class.getName()});
                return null;
            }
        }

    }

}
