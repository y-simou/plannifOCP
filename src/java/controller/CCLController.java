package controller;

import bean.CCL;
import service.CCLFacade;

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

@Named("cCLController")
@SessionScoped
public class CCLController implements Serializable {

    @EJB
    private service.CCLFacade ejbFacade;
    private List<CCL> items = null;
    private CCL selected;

    
    public String redirect(){
        return "/ccl/List?faces-redirect=true";
    }
    
    public CCLController() {
    }

    public CCL getSelected() {
        if (selected == null) {
            selected = new CCL();
        }
        return selected;
    }

    public void setSelected(CCL selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CCLFacade getFacade() {
        return ejbFacade;
    }

    public CCL prepareCreate() {
        selected = new CCL();
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

    public void delete(CCL ccl) {
      ejbFacade.remove(ccl);
            items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<CCL> getItems() {
        if (items == null) {
            items = ejbFacade.findAllAsc();
        }
        return items;
    }


    public CCL getCCL(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CCL> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CCL> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CCL.class)
    public static class CCLControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CCLController controller = (CCLController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cCLController");
            return controller.getCCL(getKey(value));
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
            if (object instanceof CCL) {
                CCL o = (CCL) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CCL.class.getName()});
                return null;
            }
        }

    }

}
