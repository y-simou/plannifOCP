package controller;

import bean.Utilisateur;
import controller.util.CookieUtil;
import controller.util.SessionUtil;
import service.UtilisateurFacade;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.context.RequestContext;

@Named("utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {

    @EJB
    private service.UtilisateurFacade ejbFacade;
    private List<Utilisateur> items = null;
    private Utilisateur selected;
    private String password;
    private String mail;
    private boolean type = false;

    public String redirect() {
        return "/utilisateur/List?faces-redirect=true";
    }
    public String forgotPass() {
        return "/utilisateur/resetPass?faces-redirect=true";
    }
    
    public void sentPassword(){
        if(ejbFacade.findByLoginMail(mail)==null){
            showErrorMessage("Mail Uncorrect !!");
        }else{
            ejbFacade.sentMail(mail);
            showMessage("Password changed, Please Check Your Mail.");
        }
    }

    public String login() {
        int conected = ejbFacade.login(selected);
        if (conected == -1) {
            showErrorMessage("Login Or Email uncorrect !!");
        } else if (conected == -2) {
            showErrorMessage("Password uncorrect !!");
        } else if (conected == 1) {
            selected = ejbFacade.findByLoginMail(selected.getLogin());
            CookieUtil.setCookie("conected", selected.getId().toString(), 60 * 60 * 24);
            SessionUtil.setAttribute("conected", selected);
            showMessage("Welcome "+ selected.getNom() + selected.getPrenom());
            return "/index?faces-redirect=true";
        } else if (conected == 2) {
            selected = ejbFacade.findByLoginMail(selected.getLogin());
            CookieUtil.setCookie("conected", selected.getId().toString(), 60 * 60 * 24);
            SessionUtil.setAttribute("conected", selected);
            showMessage("Welcome "+ selected.getNom() + selected.getPrenom());
            return "/Login?faces-redirect=true";
        }
        return "/Login?faces-redirect=true";
    }
    
    public void create() {
        RequestContext context = RequestContext.getCurrentInstance();
        int verif = verifPassword(getSelected().getPassword(), password);
        if (verif == 1) {
            int creer = ejbFacade.creerUser(selected);
            if (creer == -1) {
                context.execute("PF('UtilisateurCreateDialog').show();");
                showErrorMessage("Login Or Email allready Exist !!");

            } else {
                showMessage("Creation Successfully Complete.");
                selected = null; // Remove selection
                items = null;    // Invalidate list of items to trigger re-query.
            }

        } else {
            context.execute("PF('UtilisateurCreateDialog').show();");
            showErrorMessage("Confirmation of Passwords Failed !!");

        }
    }

    public void update() {
        ejbFacade.edit(selected);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public void delete(Utilisateur utilisateur) {
        ejbFacade.remove(utilisateur);
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    public int verifPassword(String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            return -1;
        } else {
            return 1;
        }
    }

    public void showMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  msg) );
    }

    public void showErrorMessage(String msg) {
       FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "WARNING !", msg) );
    }

    public void changeType() {
        if (type) {
            getSelected().setType(0);
        } else {
            getSelected().setType(1);
        }
        System.out.println(selected);
    }

    public UtilisateurController() {
    }

    public Utilisateur getSelected() {
        if (selected == null) {
            if(SessionUtil.getAttribute("conected")==null){
                if(CookieUtil.getCookieValue("conected")==null){
                    selected = new Utilisateur();
                }else{
                    selected = ejbFacade.find(new Long(CookieUtil.getCookieValue("conected")));
                    SessionUtil.setAttribute("conected", selected);
                }
            }else{
                selected = (Utilisateur) SessionUtil.getAttribute("conected");
            }
        }
        return selected;
    }

    public void setSelected(Utilisateur selected) {
        this.selected = selected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UtilisateurFacade getFacade() {
        return ejbFacade;
    }

    public Utilisateur prepareCreate() {
        selected = new Utilisateur();
        initializeEmbeddableKey();
        return selected;
    }

    public List<Utilisateur> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public Utilisateur getUtilisateur(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Utilisateur> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Utilisateur> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Utilisateur.class)
    public static class UtilisateurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilisateurController controller = (UtilisateurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilisateurController");
            return controller.getUtilisateur(getKey(value));
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
            if (object instanceof Utilisateur) {
                Utilisateur o = (Utilisateur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Utilisateur.class.getName()});
                return null;
            }
        }

    }

}
