package controller;

import bean.Position;
import bean.PositionMachine;
import service.PositionMachineFacade;

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
import org.primefaces.context.RequestContext;
import service.PositionFacade;

@Named("positionMachineController")
@SessionScoped
public class PositionMachineController implements Serializable {

    @EJB
    private service.PositionMachineFacade ejbFacade;
    private List<PositionMachine> items = null;
    private PositionMachine selected;
    @EJB
    private PositionFacade positionFacade;
    private Position position;

    public String redirect() {
        return "/positionMachine/List?faces-redirect=true";
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

    public void delete(PositionMachine positionMachine) {
        ejbFacade.remove(positionMachine);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }
    
    public void createPosition(){
        RequestContext context = RequestContext.getCurrentInstance();
        positionFacade.creer(position);
        selected.setPosition(null);
        context.update("position");
        selected.setPosition(position);
        System.out.println(position);
        System.out.println(selected);
        position=null;
        context.execute("PF('PositionMachineEditDialog').show();");
    }

    public PositionMachineController() {
    }

    public PositionMachine getSelected() {
        if (selected==null) {
            selected = new PositionMachine();
        }
        return selected;
    }

    public void setSelected(PositionMachine selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PositionMachineFacade getFacade() {
        return ejbFacade;
    }

    public Position getPosition() {
        if (position ==null) {
            position= new Position();
        }
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PositionMachine prepareCreate() {
        selected = new PositionMachine();
        initializeEmbeddableKey();
        return selected;
    }

    public List<PositionMachine> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public PositionMachine getPositionMachine(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PositionMachine> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PositionMachine> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PositionMachine.class)
    public static class PositionMachineControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PositionMachineController controller = (PositionMachineController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "positionMachineController");
            return controller.getPositionMachine(getKey(value));
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
            if (object instanceof PositionMachine) {
                PositionMachine o = (PositionMachine) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PositionMachine.class.getName()});
                return null;
            }
        }

    }

}
