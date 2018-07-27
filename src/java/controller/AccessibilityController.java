package controller;

import bean.Accessibility;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.Trench;
import service.AccessibilityFacade;

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

@Named("accessibilityController")
@SessionScoped
public class AccessibilityController implements Serializable {

    @EJB
    private AccessibilityFacade ejbFacade;
    private List<Accessibility> items = null;
    private Accessibility selected;
    @EJB
    private service.TrenchFacade trenchFacade;
    @EJB
    private service.ParcelFacade parcelFacade;
    @EJB
    private service.LevelLayerFacade levelLayerFacade;
    private Panel panel;
    private Trench trench;
    private Parcel parcel;
    private Long levelLayer;
    private List<Trench> trenchs;
    private List<Parcel> parcels;
    private List<LevelLayer> levelLayers;

    public void doActionPanel(AjaxBehaviorEvent event) {
        trenchs = trenchFacade.findByPanel(getPanel().getId());
    }

    public void doActionTrench(AjaxBehaviorEvent event) {
        parcels = parcelFacade.findByTrench(getTrench().getId());
    }

    public void doActionParcel(AjaxBehaviorEvent event) {
        levelLayers = levelLayerFacade.findByParcel(getParcel().getId());
    }

    public Panel getPanel() {
        if (panel == null) {
            panel = new Panel();
        }
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public Trench getTrench() {
        if (trench == null) {
            trench = new Trench();
        }
        return trench;
    }

    public void setTrench(Trench trench) {
        this.trench = trench;
    }

    public Parcel getParcel() {
        if (parcel == null) {
            parcel = new Parcel();
        }
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public Long getLevelLayer() {
        return levelLayer;
    }

    public void setLevelLayer(Long levelLayer) {
        this.levelLayer = levelLayer;
    }

    public List<Trench> getTrenchs() {
        return trenchs;
    }

    public void setTrenchs(List<Trench> trenchs) {
        this.trenchs = trenchs;
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }

    public List<LevelLayer> getLevelLayers() {
        return levelLayers;
    }

    public void setLevelLayers(List<LevelLayer> levelLayers) {
        this.levelLayers = levelLayers;
    }

    public AccessibilityController() {
    }

    public String redirect() {
        return "/accessibility/List?faces-redirect=true";
    }

    public Accessibility getSelected() {
        if (selected == null) {
            selected = new Accessibility();
        }
        return selected;
    }

    public void setSelected(Accessibility selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AccessibilityFacade getFacade() {
        return ejbFacade;
    }

    public Accessibility prepareCreate() {
        selected = new Accessibility();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(getSelected(), levelLayer);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();
        levelLayer = null;
        panel = null;
        trench = null;
        trenchs = null;
        parcel = null;
        parcels = null;
        levelLayers = null;
    }

    public void modifie() {
        ejbFacade.update(getSelected(), levelLayer);
        items = ejbFacade.findAllAsc();
        selected = null; // Remove selection
        levelLayer = null;
        panel = null;
        trench = null;
        trenchs = null;
        parcel = null;
        parcels = null;
        levelLayers = null;
    }

    public void delete(Accessibility access) {
        ejbFacade.remove(access);
        items = ejbFacade.findAllAsc();
        selected = null; // Remove selection
        levelLayer = null;
        panel = null;
        trench = null;
        trenchs = null;
        parcel = null;
        parcels = null;
        levelLayers = null;
    }

    public List<Accessibility> getItems() {
        if (items == null) {
            items = ejbFacade.findAllAsc();
        }
        return items;
    }

    public Accessibility getAccessibility(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Accessibility> getItemsAvailableSelectMany() {
        return getFacade().findAllAsc();
    }

    public List<Accessibility> getItemsAvailableSelectOne() {
        return getFacade().findAllAsc();
    }

    @FacesConverter(forClass = Accessibility.class)
    public static class AccessibilityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AccessibilityController controller = (AccessibilityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "accessibilityController");
            return controller.getAccessibility(getKey(value));
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
            if (object instanceof Accessibility) {
                Accessibility o = (Accessibility) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Accessibility.class.getName()});
                return null;
            }
        }

    }

}
