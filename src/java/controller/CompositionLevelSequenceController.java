package controller;

import bean.CompositionLevelSequence;
import service.CompositionLevelSequenceFacade;

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

@Named("compositionLevelSequenceController")
@SessionScoped
public class CompositionLevelSequenceController implements Serializable {

    @EJB
    private service.CompositionLevelSequenceFacade ejbFacade;
    private List<CompositionLevelSequence> items = null;
    private CompositionLevelSequence selected;

    public CompositionLevelSequenceController() {
    }

    public String redirect() {
        return "/compositionLevelSequence/List?faces-redirect=true";
    }

    public CompositionLevelSequence getSelected() {
        if (selected == null) {
            selected = new CompositionLevelSequence();
        }

        return selected;
    }

    public void setSelected(CompositionLevelSequence selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CompositionLevelSequenceFacade getFacade() {
        return ejbFacade;
    }

    public CompositionLevelSequence prepareCreate() {
        selected = new CompositionLevelSequence();
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

    public void delete(CompositionLevelSequence compositionLevelSequence) {
        ejbFacade.remove(compositionLevelSequence);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<CompositionLevelSequence> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    public CompositionLevelSequence getCompositionLevelSequence(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<CompositionLevelSequence> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<CompositionLevelSequence> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = CompositionLevelSequence.class)
    public static class CompositionLevelSequenceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompositionLevelSequenceController controller = (CompositionLevelSequenceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "compositionLevelSequenceController");
            return controller.getCompositionLevelSequence(getKey(value));
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
            if (object instanceof CompositionLevelSequence) {
                CompositionLevelSequence o = (CompositionLevelSequence) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CompositionLevelSequence.class.getName()});
                return null;
            }
        }

    }

}
