package controller;

import bean.StatutBlock;
import service.StatutBlockFacade;

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

@Named("statutBlockController")
@SessionScoped
public class StatutBlockController implements Serializable {

    @EJB
    private service.StatutBlockFacade ejbFacade;
    private List<StatutBlock> items = null;
    private StatutBlock selected;

    public StatutBlockController() {
    }

    public StatutBlock getSelected() {
        if (selected == null) {
            selected = new StatutBlock();
        }
        return selected;
    }

    public void setSelected(StatutBlock selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StatutBlockFacade getFacade() {
        return ejbFacade;
    }

    public StatutBlock prepareCreate() {
        selected = new StatutBlock();
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

    public List<StatutBlock> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }


    public StatutBlock getStatutBlock(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<StatutBlock> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<StatutBlock> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = StatutBlock.class)
    public static class StatutBlockControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StatutBlockController controller = (StatutBlockController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "statutBlockController");
            return controller.getStatutBlock(getKey(value));
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
            if (object instanceof StatutBlock) {
                StatutBlock o = (StatutBlock) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), StatutBlock.class.getName()});
                return null;
            }
        }

    }

}
