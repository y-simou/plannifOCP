package controller;

import bean.ExitPanel;
import service.exitPanelFacade;

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

@Named("exitPanelController")
@SessionScoped
public class exitPanelController implements Serializable {

    @EJB
    private service.exitPanelFacade ejbFacade;
    private List<ExitPanel> items = null;
    private ExitPanel selected;

    public String redirect() {
        return "/exitPanel/List?faces-redirect=true";
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

    public void delete(ExitPanel exitPanel) {
        ejbFacade.remove(exitPanel);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public exitPanelController() {
    }

    public ExitPanel getSelected() {
        if (selected==null) {
            selected = new ExitPanel();
        }
        return selected;
    }

    public void setSelected(ExitPanel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private exitPanelFacade getFacade() {
        return ejbFacade;
    }

    public ExitPanel prepareCreate() {
        selected = new ExitPanel();
        initializeEmbeddableKey();
        return selected;
    }

    public List<ExitPanel> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public ExitPanel getexitPanel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ExitPanel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ExitPanel> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ExitPanel.class)
    public static class exitPanelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            exitPanelController controller = (exitPanelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "exitPanelController");
            return controller.getexitPanel(getKey(value));
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
            if (object instanceof ExitPanel) {
                ExitPanel o = (ExitPanel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ExitPanel.class.getName()});
                return null;
            }
        }

    }

}
