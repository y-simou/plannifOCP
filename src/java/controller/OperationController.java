package controller;

import bean.Operation;
import service.OperationFacade;

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

@Named("operationController")
@SessionScoped
public class OperationController implements Serializable {

    @EJB
    private service.OperationFacade ejbFacade;
    private List<Operation> items = null;
    private Operation selected;

    public OperationController() {
    }

    public String redirect() {
        return "/operation/List?faces-redirect=true";
    }

    public Operation getSelected() {
        if (selected == null) {
            selected = new Operation();
        }
        return selected;
    }

    public void setSelected(Operation selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private OperationFacade getFacade() {
        return ejbFacade;
    }

    public Operation prepareCreate() {
        selected = new Operation();
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

    public void delete(Operation operation) {
        ejbFacade.delete(operation);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Operation> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Operation getOperation(int id) {
        return getFacade().find(id);
    }

    public List<Operation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Operation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Operation.class)
    public static class OperationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OperationController controller = (OperationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "operationController");
            return controller.getOperation(getKey(value));
        }

        int getKey(String value) {
            int key;
            key = Integer.parseInt(value);
            return key;
        }

        String getStringKey(int value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Operation) {
                Operation o = (Operation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Operation.class.getName()});
                return null;
            }
        }

    }

}
