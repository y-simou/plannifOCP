/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import java.util.Arrays;
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
     * @param nom
     * @return 
     */
    public static Cookie getCookie(String nom) {
        System.out.println("dkhlt");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("khdit request");
        System.out.println(request);
        Cookie[] cookies = request.getCookies();
        System.out.println("list cookies");
        System.out.println(Arrays.toString(cookies));
        if (cookies != null) {
            System.out.println("list cookies makhawyach");
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
            System.out.println("cookie :: "+cookie.getValue());
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
