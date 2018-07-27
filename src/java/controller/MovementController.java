package controller;

import bean.Machine;
import bean.Movement;
import bean.PositionMachine;
import service.MovementFacade;

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
import javax.faces.event.AjaxBehaviorEvent;

@Named("movementController")
@SessionScoped
public class MovementController implements Serializable {

    @EJB
    private service.MovementFacade ejbFacade;
    @EJB
    private service.PositionMachineFacade positionMachineFacade;
    private List<Movement> items = null;
    private Movement selected;
    private Machine machine;
    private List<PositionMachine> positions;

    public void doActionMachine(AjaxBehaviorEvent event) {
        positions = positionMachineFacade.findByMachine(getMachine().getId());
        machine = null;
    }

    public Machine getMachine() {
        if (machine == null) {
            machine = new Machine();
        }
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public List<PositionMachine> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionMachine> positions) {
        this.positions = positions;
    }

    public MovementController() {
    }

    public String redirect() {
        return "/movement/List?faces-redirect=true";
    }

    public Movement getSelected() {
        if (selected == null) {
            selected = new Movement();
        }
        return selected;
    }

    public void setSelected(Movement selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MovementFacade getFacade() {
        return ejbFacade;
    }

    public Movement prepareCreate() {
        selected = new Movement();
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

    public void delete(Movement movement) {
        ejbFacade.remove(movement);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Movement> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    public Movement getMovement(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Movement> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Movement> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Movement.class)
    public static class MovementControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MovementController controller = (MovementController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "movementController");
            return controller.getMovement(getKey(value));
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
            if (object instanceof Movement) {
                Movement o = (Movement) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Movement.class.getName()});
                return null;
            }
        }

    }

}
