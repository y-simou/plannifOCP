package controller;

import bean.ChemicalComponentLayer;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.Trench;
import service.ChemicalComponentLayerFacade;

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

@Named("chemicalComponentLayerController")
@SessionScoped
public class ChemicalComponentLayerController implements Serializable {

    @EJB
    private service.ChemicalComponentLayerFacade ejbFacade;
    private List<ChemicalComponentLayer> items = null;
    private ChemicalComponentLayer selected;
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
        panel = null;
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

    public ChemicalComponentLayerController() {
    }

    public String redirect() {
        return "/chemicalComponentLayer/List?faces-redirect=true";
    }

    public ChemicalComponentLayer getSelected() {
        if (selected == null) {
            selected = new ChemicalComponentLayer();
        }
        return selected;
    }

    public void setSelected(ChemicalComponentLayer selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ChemicalComponentLayerFacade getFacade() {
        return ejbFacade;
    }

    public ChemicalComponentLayer prepareCreate() {
        selected = new ChemicalComponentLayer();
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

    public void delete(ChemicalComponentLayer chemicalComponentLayer) {
        ejbFacade.remove(chemicalComponentLayer);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<ChemicalComponentLayer> getItems() {
        return ejbFacade.findAllAsc();
    }

    public ChemicalComponentLayer getChemicalComponentLayer(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ChemicalComponentLayer> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ChemicalComponentLayer> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ChemicalComponentLayer.class)
    public static class ChemicalComponentLayerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ChemicalComponentLayerController controller = (ChemicalComponentLayerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "chemicalComponentLayerController");
            return controller.getChemicalComponentLayer(getKey(value));
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
            if (object instanceof ChemicalComponentLayer) {
                ChemicalComponentLayer o = (ChemicalComponentLayer) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ChemicalComponentLayer.class.getName()});
                return null;
            }
        }

    }

}
