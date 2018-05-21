/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yassine.SIMOU
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    @PersistenceContext(unitName = "plannOCPPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }
    
    public int login(Utilisateur user){
        List<Utilisateur> users = getEntityManager().createQuery("SELECT u FROM Utilisateur u WHERE"
                + " u.login='"+ user.getLogin() +"' OR u.mail='"+ user.getMail() +"'").getResultList();
        if(users.isEmpty()){
            return -1;
        }else {
            Utilisateur u = users.get(0);
            if(!u.getPassword().equals(user.getPassword())){
                return -2;
            }else if (u.getType()==0) {
                return 1;
            }else {
                return 2;
            }
        }
    }
    
    public int creerUser(Utilisateur utilisateur){
        if(findByLoginOrMail(utilisateur.getLogin(), utilisateur.getMail())==null){
            create(utilisateur);
            return 1;
        }else {
            return -1;
        }
    }
    
    public Utilisateur findByLoginOrMail(String login ,String mail){
        List<Utilisateur> res = getEntityManager().createQuery("SELECT u FROM Utilisateur u where u.login='"+ login +"' OR u.mail='"+ mail +"'").getResultList();
        if(res.isEmpty()){
            return null;
        }else {
            return res.get(0);
        }
    }    
    public Utilisateur findByLoginMail(String login){
        List<Utilisateur> res = getEntityManager().createQuery("SELECT u FROM Utilisateur u where u.login='"+ login +"' OR u.mail='"+ login +"'").getResultList();
        if(res.isEmpty()){
            return null;
        }else {
            return res.get(0);
        }
    }    
    
    public void sentMail(String mail){
        
    }
}
