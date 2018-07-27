package controller;

import bean.Stock;
import service.StockFacade;

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

@Named("stockController")
@SessionScoped
public class StockController implements Serializable {

    @EJB
    private service.StockFacade ejbFacade;
    private List<Stock> items = null;
    private Stock selected;

    public StockController() {
    }

    public String redirect() {
        return "/stock/List?faces-redirect=true";
    }

    public Stock getSelected() {
        if (selected == null) {
            selected = new Stock();
        }
        return selected;
    }

    public void setSelected(Stock selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private StockFacade getFacade() {
        return ejbFacade;
    }

    public Stock prepareCreate() {
        selected = new Stock();
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

    public void delete(Stock stock) {
        ejbFacade.delete(stock);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<Stock> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    public Stock getStock(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Stock> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Stock> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Stock.class)
    public static class StockControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StockController controller = (StockController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "stockController");
            return controller.getStock(getKey(value));
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
            if (object instanceof Stock) {
                Stock o = (Stock) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Stock.class.getName()});
                return null;
            }
        }

    }

}
