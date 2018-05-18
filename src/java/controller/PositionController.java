package controller;

import bean.Position;
import service.PositionFacade;

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

@Named("positionController")
@SessionScoped
public class PositionController implements Serializable {

    @EJB
    private service.PositionFacade ejbFacade;
    private List<Position> items = null;
    private Position selected;

    public PositionController() {
    }

    public String redirect() {
        return "/position/List?faces-redirect=true";
    }

    public Position getSelected() {
        if (selected == null) {
            selected = new Position();
        }
        return selected;
    }

    public void setSelected(Position selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PositionFacade getFacade() {
        return ejbFacade;
    }

    public Position prepareCreate() {
        selected = new Position();
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

    public void delete(Position position) {
        ejbFacade.delete(position);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Position> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Position getPosition(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Position> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Position> getItemsAvailableSelectOne() {
        return getFacade().findAllDesc();
    }

    @FacesConverter(forClass = Position.class)
    public static class PositionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PositionController controller = (PositionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "positionController");
            return controller.getPosition(getKey(value));
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
            if (object instanceof Position) {
                Position o = (Position) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Position.class.getName()});
                return null;
            }
        }

    }

}
