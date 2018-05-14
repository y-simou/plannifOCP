package controller;

import bean.Layer;
import service.LayerFacade;

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

@Named("layerController")
@SessionScoped
public class LayerController implements Serializable {

    @EJB
    private service.LayerFacade ejbFacade;
    private List<Layer> items = null;
    private Layer selected;

    public LayerController() {
    }

    public String redirect() {
        return "/layer/List?faces-redirect=true";
    }

    public Layer getSelected() {
        if (selected == null) {
            selected = new Layer();
        }
        return selected;
    }

    public void setSelected(Layer selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LayerFacade getFacade() {
        return ejbFacade;
    }

    public Layer prepareCreate() {
        selected = new Layer();
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

    public void delete(Layer layer) {
        ejbFacade.remove(layer);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Layer> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Layer getLayer(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Layer> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Layer> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Layer.class)
    public static class LayerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LayerController controller = (LayerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "layerController");
            return controller.getLayer(getKey(value));
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
            if (object instanceof Layer) {
                Layer o = (Layer) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Layer.class.getName()});
                return null;
            }
        }

    }

}
