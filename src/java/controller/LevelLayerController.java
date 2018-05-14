package controller;

import bean.LevelLayer;
import service.LevelLayerFacade;

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

@Named("levelLayerController")
@SessionScoped
public class LevelLayerController implements Serializable {

    @EJB
    private service.LevelLayerFacade ejbFacade;
    private List<LevelLayer> items = null;
    private LevelLayer selected;

    public LevelLayerController() {
    }

    public String redirect() {
        return "/levelLayer/List?faces-redirect=true";
    }

    public LevelLayer getSelected() {
        if (selected == null) {
            selected = new LevelLayer();
        }
        return selected;
    }

    public void setSelected(LevelLayer selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LevelLayerFacade getFacade() {
        return ejbFacade;
    }

    public LevelLayer prepareCreate() {
        selected = new LevelLayer();
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

    public void delete(LevelLayer levelLayer) {
        ejbFacade.delete(levelLayer);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<LevelLayer> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public LevelLayer getLevelLayer(int id) {
        return getFacade().find(id);
    }

    public List<LevelLayer> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<LevelLayer> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = LevelLayer.class)
    public static class LevelLayerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LevelLayerController controller = (LevelLayerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "levelLayerController");
            return controller.getLevelLayer(getKey(value));
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
            if (object instanceof LevelLayer) {
                LevelLayer o = (LevelLayer) object;
                return getStringKey(o.getNum());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), LevelLayer.class.getName()});
                return null;
            }
        }

    }

}
