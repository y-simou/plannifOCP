/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author delll
 */
public class CookieUtil {

    /**
     * Méthode utilitaire gérant la récupération de la valeur d'un CookieUtil
     * donné depuis la requête HTTP.
     */
    public static Cookie getCookie(String nom) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null && nom.equals(cookie.getName())) {
                    System.out.println(cookie);
                    System.out.println(cookie.getName());
                    System.out.println(cookie.getValue());
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(String nom) {
        Cookie cookie = getCookie(nom);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public static void setCookie(String nom, String valeur, int maxAge) {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie cookie = new Cookie(nom, valeur);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/plaanOCP/faces");
        System.out.println("creating cookie");
        response.addCookie(cookie);
    }

    public static void remove(String nom) {
        System.out.println("remove");
        setCookie("conected", "", 0);

    }

}
