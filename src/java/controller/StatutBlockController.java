package controller;

import bean.Block;
import bean.LevelLayer;
import bean.Panel;
import bean.Parcel;
import bean.StatutBlock;
import bean.Trench;
import service.StatutBlockFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

@Named("statutBlockController")
@SessionScoped
public class StatutBlockController implements Serializable {

    @EJB
    private service.StatutBlockFacade ejbFacade;
    private List<StatutBlock> items = null;
    private StatutBlock selected;
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
    private Long block;
    private List<Trench> trenchs;
    private List<Parcel> parcels;
    private List<LevelLayer> levelLayers;
    private List<Block> blocks;

    public void doActionPanel(AjaxBehaviorEvent event) {
        System.out.println("panle:"+panel);
        trenchs = trenchFacade.findByPanel(panel.getId());
        System.out.println("trenchs:"+trench);
    }

    public void doActionTrench(AjaxBehaviorEvent event) {
        parcels = parcelFacade.findByTrench(trench.getId());
    }

    public void doActionParcel(AjaxBehaviorEvent event) {
        levelLayers = levelLayerFacade.findByParcel(parcel.getId());
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

    public Long getBlock() {
        return block;
    }

    public void setBlock(Long block) {
        this.block = block;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public boolean filterByDate(Object value, Object filter, Locale locale) {

        if (filter == null) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return !((Date) value).before((Date) filter);
    }

    public StatutBlockController() {
    }

    public String redirect() {
        return "/statutBlock/List?faces-redirect=true";
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
        ejbFacade.create(getSelected(),block);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();    // Invalidate list of items to trigger re-query.
    }

    public void update() {
        ejbFacade.update(getSelected(),block);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();    // Invalidate list of items to trigger re-query.
    }

    public void delete(StatutBlock statutBlock) {
        ejbFacade.remove(statutBlock);
        selected = null; // Remove selection
        items = ejbFacade.findAllAsc();    // Invalidate list of items to trigger re-query.
    }

    public List<StatutBlock> getItems() {
        if (items == null) {
            items = ejbFacade.findAllAsc();
        }
        return items;
    }

    public StatutBlock getStatutBlock(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<StatutBlock> getItemsAvailableSelectMany() {
        return getFacade().findAllAsc();
    }

    public List<StatutBlock> getItemsAvailableSelectOne() {
        return getFacade().findAllAsc();
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
