package controller;

import bean.Poste;
import service.PosteFacade;

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

@Named("posteController")
@SessionScoped
public class PosteController implements Serializable {

    @EJB
    private service.PosteFacade ejbFacade;
    private List<Poste> items = null;
    private Poste selected;

    public String redirect() {
        return "/poste/List?faces-redirect=true";
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

    public void delete(Poste poste) {
        ejbFacade.delete(poste.getId());
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public PosteController() {
    }

    public Poste getSelected() {
        if (selected==null) {
            selected = new Poste();
        }
        return selected;
    }

    public void setSelected(Poste selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PosteFacade getFacade() {
        return ejbFacade;
    }

    public Poste prepareCreate() {
        selected = new Poste();
        initializeEmbeddableKey();
        return selected;
    }

    public List<Poste> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Poste getPoste(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Poste> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Poste> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Poste.class)
    public static class PosteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PosteController controller = (PosteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "posteController");
            return controller.getPoste(getKey(value));
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
            if (object instanceof Poste) {
                Poste o = (Poste) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Poste.class.getName()});
                return null;
            }
        }

    }

}
