package controller;

import bean.SequenceLevel;
import service.SequenceLevelFacade;

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

@Named("sequenceLevelController")
@SessionScoped
public class SequenceLevelController implements Serializable {

    @EJB
    private service.SequenceLevelFacade ejbFacade;
    private List<SequenceLevel> items = null;
    private SequenceLevel selected;

    public SequenceLevelController() {
    }

    public String redirect() {
        return "/sequenceLevel/List?faces-redirect=true";
    }

    public SequenceLevel getSelected() {
        if (selected == null) {
            selected = new SequenceLevel();
        }
        return selected;
    }

    public void setSelected(SequenceLevel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SequenceLevelFacade getFacade() {
        return ejbFacade;
    }

    public SequenceLevel prepareCreate() {
        selected = new SequenceLevel();
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

    public void delete(SequenceLevel sequenceLevel) {
        ejbFacade.delete(sequenceLevel.getId());
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<SequenceLevel> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public SequenceLevel getSequenceLevel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<SequenceLevel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<SequenceLevel> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = SequenceLevel.class)
    public static class SequenceLevelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SequenceLevelController controller = (SequenceLevelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sequenceLevelController");
            return controller.getSequenceLevel(getKey(value));
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
            if (object instanceof SequenceLevel) {
                SequenceLevel o = (SequenceLevel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), SequenceLevel.class.getName()});
                return null;
            }
        }

    }

}
