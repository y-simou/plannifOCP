package controller;

import bean.AvailabilityMachine;
import service.AvailabilityMachineFacade;

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

@Named("availabilityMachineController")
@SessionScoped
public class AvailabilityMachineController implements Serializable {

    @EJB
    private service.AvailabilityMachineFacade ejbFacade;
    private List<AvailabilityMachine> items = null;
    private AvailabilityMachine selected;

    public AvailabilityMachineController() {
    }

    public AvailabilityMachine getSelected() {
        if(selected==null)
            selected = new AvailabilityMachine();
        return selected;
    }

    public void setSelected(AvailabilityMachine selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AvailabilityMachineFacade getFacade() {
        return ejbFacade;
    }

    public AvailabilityMachine prepareCreate() {
        selected = new AvailabilityMachine();
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

    public List<AvailabilityMachine> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public AvailabilityMachine getAvailabilityMachine(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<AvailabilityMachine> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<AvailabilityMachine> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = AvailabilityMachine.class)
    public static class AvailabilityMachineControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvailabilityMachineController controller = (AvailabilityMachineController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "availabilityMachineController");
            return controller.getAvailabilityMachine(getKey(value));
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
            if (object instanceof AvailabilityMachine) {
                AvailabilityMachine o = (AvailabilityMachine) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AvailabilityMachine.class.getName()});
                return null;
            }
        }

    }

}
