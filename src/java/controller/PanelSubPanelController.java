package controller;

import bean.PanelSubPanel;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.PanelSubPanelFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("panelSubPanelController")
@SessionScoped
public class PanelSubPanelController implements Serializable {

    @EJB
    private service.PanelSubPanelFacade ejbFacade;
    private List<PanelSubPanel> items = null;
    private PanelSubPanel selected;

    public PanelSubPanelController() {
    }

    public PanelSubPanel getSelected() {
        if (selected==null) {
            selected = new PanelSubPanel();
        }
        return selected;
    }

    public void setSelected(PanelSubPanel selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PanelSubPanelFacade getFacade() {
        return ejbFacade;
    }

    public PanelSubPanel prepareCreate() {
        selected = new PanelSubPanel();
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

    public void delete(PanelSubPanel panelSubPanel) {
        ejbFacade.remove(panelSubPanel);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public List<PanelSubPanel> getItems() {
        if (items == null) {
            items = ejbFacade.findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public PanelSubPanel getPanelSubPanel(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PanelSubPanel> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PanelSubPanel> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PanelSubPanel.class)
    public static class PanelSubPanelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PanelSubPanelController controller = (PanelSubPanelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "panelSubPanelController");
            return controller.getPanelSubPanel(getKey(value));
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
            if (object instanceof PanelSubPanel) {
                PanelSubPanel o = (PanelSubPanel) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PanelSubPanel.class.getName()});
                return null;
            }
        }

    }

}
