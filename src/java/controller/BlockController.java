package controller;

import bean.Block;
import service.BlockFacade;

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

@Named("blockController")
@SessionScoped
public class BlockController implements Serializable {

    @EJB
    private service.BlockFacade ejbFacade;
    private List<Block> items = null;
    private Block selected;

    public BlockController() {
    }

    public String redirect() {
        return "/block/List?faces-redirect=true";
    }

    public Block getSelected() {
        if (selected == null) {
            selected = new Block();
        }
        return selected;
    }

    public void setSelected(Block selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private BlockFacade getFacade() {
        return ejbFacade;
    }

    public Block prepareCreate() {
        selected = new Block();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        ejbFacade.create(selected);
        selected = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void update() {
        ejbFacade.edit(selected);
        selected = null;
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void delete(Block block) {
        ejbFacade.delete(block.getId());
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Block> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    public Block getBlock(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Block> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Block> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Block.class)
    public static class BlockControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BlockController controller = (BlockController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "blockController");
            return controller.getBlock(getKey(value));
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
            if (object instanceof Block) {
                Block o = (Block) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Block.class.getName()});
                return null;
            }
        }

    }

}
