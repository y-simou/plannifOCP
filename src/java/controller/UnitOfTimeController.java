package controller;

import bean.UnitOfTime;
import service.UnitOfTimeFacade;

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

@Named("unitOfTimeController")
@SessionScoped
public class UnitOfTimeController implements Serializable {

    @EJB
    private service.UnitOfTimeFacade ejbFacade;
    private List<UnitOfTime> items = null;
    private UnitOfTime selected;

    public UnitOfTimeController() {
    }

    public UnitOfTime getSelected() {
        if (selected == null) {
            selected = new UnitOfTime();
        }
        return selected;
    }

    public void setSelected(UnitOfTime selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UnitOfTimeFacade getFacade() {
        return ejbFacade;
    }

    public UnitOfTime prepareCreate() {
        selected = new UnitOfTime();
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

    public List<UnitOfTime> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }


    public UnitOfTime getUnitOfTime(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<UnitOfTime> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<UnitOfTime> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = UnitOfTime.class)
    public static class UnitOfTimeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UnitOfTimeController controller = (UnitOfTimeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "unitOfTimeController");
            return controller.getUnitOfTime(getKey(value));
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
            if (object instanceof UnitOfTime) {
                UnitOfTime o = (UnitOfTime) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UnitOfTime.class.getName()});
                return null;
            }
        }

    }

}
