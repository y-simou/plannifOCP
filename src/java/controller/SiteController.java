package controller;

import bean.Site;
import service.SiteFacade;

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

@Named("siteController")
@SessionScoped
public class SiteController implements Serializable {

    @EJB
    private service.SiteFacade ejbFacade;
    private List<Site> items = null;
    private Site selected;

    public String redirect() {
        return "/site/List?faces-redirect=true";
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

    public void delete(Site site) {
        ejbFacade.remove(site);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public SiteController() {
    }

    public Site getSelected() {
        if (selected==null) {
            selected = new Site();
        }
        return selected;
    }

    public void setSelected(Site selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SiteFacade getFacade() {
        return ejbFacade;
    }

    public Site prepareCreate() {
        selected = new Site();
        initializeEmbeddableKey();
        return selected;
    }

    public List<Site> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Site getSite(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Site> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Site> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Site.class)
    public static class SiteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SiteController controller = (SiteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "siteController");
            return controller.getSite(getKey(value));
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
            if (object instanceof Site) {
                Site o = (Site) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Site.class.getName()});
                return null;
            }
        }

    }

}
