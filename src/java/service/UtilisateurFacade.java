/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Utilisateur;
import controller.util.HashageUtil;
import java.security.SecureRandom;
import java.util.List;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

    public pageHtmlString pageHtmlString = new pageHtmlString();
    String username = "emines.ocp@gmail.com";
    String password = "emines2018ocp";
    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static SecureRandom random = new SecureRandom();
    int len = 7;

    public int login(Utilisateur user) {
         Utilisateur u = findByLoginMail(user.getLogin());
        if (u==null) {
            return -1;
        } else {
            if (!u.getPassword().equals(HashageUtil.sha256(user.getPassword()))) {
                return -2;
            } else if (u.getType() == 0) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    public int creerUser(Utilisateur utilisateur) {
        String mail = utilisateur.getMail().toUpperCase();
        if (findByLoginOrMail(utilisateur.getLogin(), mail) == null) {
            utilisateur.setMail(mail);
            create(utilisateur);
            utilisateur = findByLoginMail(mail);
            if (utilisateur != null) {
                String pass = generatePassword(len, mail);
                utilisateur.setPassword(HashageUtil.sha256(pass));
                sentMail(mail, pageHtmlString.signHtmlPage(utilisateur.getNom() + " " + utilisateur.getPrenom(), pass,utilisateur.getLogin()));
                edit(utilisateur);
            }
            return 1;
        } else {
            return -1;
        }
    }

    public Utilisateur findByLoginOrMail(String login, String mail) {
        List<Utilisateur> res = getEntityManager().createQuery("SELECT u FROM Utilisateur u where u.login='" + login + "' OR u.mail='" + mail + "'").getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            return res.get(0);
        }
    }

    public Utilisateur findByLoginMail(String login) {
        List<Utilisateur> res = getEntityManager().createQuery("SELECT u FROM Utilisateur u where u.login='" + login + "' OR u.mail='" + login + "'").getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            return res.get(0);
        }
    }

    public static String generatePassword(int len, String dic) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

    public void resetMail(String mail) {
        Utilisateur utilisateur = findByLoginMail(mail);
        if (utilisateur != null) {
            String pass = generatePassword(len, mail);
            utilisateur.setPassword(HashageUtil.sha256(pass));
            sentMail(mail, pageHtmlString.sendHtmlEmail(utilisateur.getNom() + " " + utilisateur.getPrenom(), pass));
            edit(utilisateur);
        }

    }

    public void sentMail(String mail, String page) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
//        office.365.com
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("OCP");
            message.setContent(page, "text/html");
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }
}
