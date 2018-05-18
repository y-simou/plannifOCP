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
}
