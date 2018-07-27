package controller;

import bean.Block;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.Treatment;
import bean.Trench;
import service.TreatmentFacade;

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

@Named("treatmentController")
@SessionScoped
public class TreatmentController implements Serializable {

    @EJB
    private service.TreatmentFacade ejbFacade;
    private List<Treatment> items = null;
    private Treatment selected;
    @EJB
    private service.TrenchFacade trenchFacade;
    @EJB
    private service.ParcelFacade parcelFacade;
    @EJB
    private service.LevelLayerFacade levelLayerFacade;
    @EJB
    private service.BlockFacade blockFacade;
    private Panel panel;
    private Trench trench;
    private Parcel parcel;
    private Long levelLayer;
    private List<Trench> trenchs;
    private List<Parcel> parcels;
    private List<LevelLayer> levelLayers;
    private List<Block> blocks;

    public void doActionPanel(AjaxBehaviorEvent event) {
        trenchs = trenchFacade.findByPanel(getPanel().getId());
    }

    public void doActionTrench(AjaxBehaviorEvent event) {
        parcels = parcelFacade.findByTrench(getTrench().getId());
    }

    public void doActionParcel(AjaxBehaviorEvent event) {
        levelLayers = levelLayerFacade.findByParcel(getParcel().getId());
    }

    public void doActionLevel(AjaxBehaviorEvent event) {
        blocks = blockFacade.findByLevel(levelLayer);
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
//        if (levelLayer == null) {
//            levelLayer = new LevelLayer();
//        }
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

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public TreatmentController() {
    }

    public String redirect() {
        return "/treatment/List?faces-redirect=true";
    }

    public Treatment getSelected() {
        if (selected == null) {
            selected = new Treatment();
        }
        return selected;
    }

    public void setSelected(Treatment selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TreatmentFacade getFacade() {
        return ejbFacade;
    }

    public Treatment prepareCreate() {
        selected = new Treatment();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(selected);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();    // Invalidate list of items to trigger re-query.
        levelLayer = null;
        panel = null;
        trench = null;
        trenchs = null;
        parcel = null;
        parcels = null;
        levelLayers = null;
        blocks= null;
    }

    public void update() {
        ejbFacade.edit(selected);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();    // Invalidate list of items to trigger re-query.
         levelLayer = null;
        panel = null;
        trench = null;
        trenchs = null;
        parcel = null;
        parcels = null;
        levelLayers = null;
        blocks= null;
    }

    public void delete(Treatment treatment) {
        ejbFacade.remove(treatment);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();    // Invalidate list of items to trigger re-query.
    }

    public List<Treatment> getItems() {
        if (items == null) {
            items = ejbFacade.findAllAsc();
        }
        return items;
    }

    public Treatment getTreatment(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Treatment> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Treatment> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Treatment.class)
    public static class TreatmentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TreatmentController controller = (TreatmentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "treatmentController");
            return controller.getTreatment(getKey(value));
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
            if (object instanceof Treatment) {
                Treatment o = (Treatment) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Treatment.class.getName()});
                return null;
            }
        }

    }

}
