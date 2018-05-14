package controller;

import bean.ChemicalComponent;
import service.ChemicalComponentFacade;

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

@Named("chemicalComponentController")
@SessionScoped
public class ChemicalComponentController implements Serializable {

    @EJB
    private service.ChemicalComponentFacade ejbFacade;
    private List<ChemicalComponent> items = null;
    private ChemicalComponent selected;

    public String redirect() {
        return "/chemicalComponent/List?faces-redirect=true";
    }

    public ChemicalComponentController() {
    }

    public ChemicalComponent getSelected() {
        if (selected == null) {
            selected = new ChemicalComponent();
        }
        return selected;
    }

    public void setSelected(ChemicalComponent selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ChemicalComponentFacade getFacade() {
        return ejbFacade;
    }

    public ChemicalComponent prepareCreate() {
        selected = new ChemicalComponent();
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

    public void delete(ChemicalComponent chemicalComponent) {
        ejbFacade.remove(chemicalComponent);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<ChemicalComponent> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public ChemicalComponent getChemicalComponent(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ChemicalComponent> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ChemicalComponent> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ChemicalComponent.class)
    public static class ChemicalComponentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ChemicalComponentController controller = (ChemicalComponentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "chemicalComponentController");
            return controller.getChemicalComponent(getKey(value));
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
            if (object instanceof ChemicalComponent) {
                ChemicalComponent o = (ChemicalComponent) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ChemicalComponent.class.getName()});
                return null;
            }
        }

    }

}
