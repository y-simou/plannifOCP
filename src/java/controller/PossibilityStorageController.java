package controller;

import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.PossibilityStorage;
import bean.Trench;
import service.PossibilityStorageFacade;

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

@Named("possibilityStorageController")
@SessionScoped
public class PossibilityStorageController implements Serializable {

    @EJB
    private service.PossibilityStorageFacade ejbFacade;
    private List<PossibilityStorage> items = null;
    private PossibilityStorage selected;
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
        trenchs = trenchFacade.findByPanel(panel.getId());
        panel = null;
    }

    public void doActionTrench(AjaxBehaviorEvent event) {
        parcels = parcelFacade.findByTrench(trench.getId());
        trench = null;
    }

    public void doActionParcel(AjaxBehaviorEvent event) {
        levelLayers = levelLayerFacade.findByParcel(parcel.getId());
        parcel = null;
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

    public PossibilityStorageController() {
    }

    public String redirect() {
        return "/possibilityStorage/List?faces-redirect=true";
    }

    public PossibilityStorage getSelected() {
        if (selected == null) {
            selected = new PossibilityStorage();
        }
        return selected;
    }

    public void setSelected(PossibilityStorage selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PossibilityStorageFacade getFacade() {
        return ejbFacade;
    }

    public PossibilityStorage prepareCreate() {
        selected = new PossibilityStorage();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(getSelected(), levelLayer);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void update() {
        ejbFacade.update(getSelected(), levelLayer);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void delete(PossibilityStorage possibilityStorage) {
        ejbFacade.remove(possibilityStorage);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<PossibilityStorage> getItems() {
        items = ejbFacade.findAll();
        return items;
    }

    public PossibilityStorage getPossibilityStorage(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PossibilityStorage> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PossibilityStorage> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PossibilityStorage.class)
    public static class PossibilityStorageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PossibilityStorageController controller = (PossibilityStorageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "possibilityStorageController");
            return controller.getPossibilityStorage(getKey(value));
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
            if (object instanceof PossibilityStorage) {
                PossibilityStorage o = (PossibilityStorage) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PossibilityStorage.class.getName()});
                return null;
            }
        }

    }

}
