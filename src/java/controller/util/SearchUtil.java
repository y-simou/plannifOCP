/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import controller.util.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ACER
 */
public class SearchUtil {

    public static String addConstraint(String beanAbrev, String atributeName, String operator, Object value) {
        boolean condition = value != null;
        if (value != null && value.getClass().getSimpleName().equals("String")) {
            condition = condition && !value.equals("");
        }
        if (condition && !atributeName.equals("")) {
            return " AND " + beanAbrev + "." + atributeName + " " + operator + " '" + value + "'";
        }
        return "";
    }

    public static String addConstraintOr(String beanAbrev, String atributeName, String operator, Object value) {
        boolean condition = value != null;
        if (value != null && value.getClass().getSimpleName().equals("String")) {
            condition = condition && !value.equals("");
        }
        if (condition) {
            return " OR " + beanAbrev + "." + atributeName + " " + operator + " '" + value + "'";
        }
        return "";
    }

    public static String addConstraintOr(String beanAbrev, String atributeName, String operator, List values) {
        String query = " AND ( 1=0 ";
        if (values != null && !values.isEmpty()) {
            for (Object value : values) {
                query += addConstraintOr(beanAbrev, atributeName, operator, value);
            }
            return query+")";
        }
        return "";
    }

    public static String addConstraintMinMax(String beanAbrev, String atributeName, Object valueMin, Object valueMax) {
        String requette = "";
        if (valueMin != null) {
            requette += " AND " + beanAbrev + "." + atributeName + " >= '" + valueMin + "'";
        }
        if (valueMax != null) {
            requette += " AND " + beanAbrev + "." + atributeName + " <= '" + valueMax + "'";
        }
        return requette;
    }
    public static String addConstraintMinMaxStrict(String beanAbrev, String atributeName, Object valueMin, Object valueMax) {
        String requette = "";
        if (valueMin != null) {
            requette += " AND " + beanAbrev + "." + atributeName + " > '" + valueMin + "'";
        }
        if (valueMax != null) {
            requette += " AND " + beanAbrev + "." + atributeName + " < '" + valueMax + "'";
        }
        return requette;
    }

    public static String addConstraintDate(String beanAbrev, String atributeName, String operator, Date value) {
        return addConstraint(beanAbrev, atributeName, operator, DateUtil.convertFormUtilToSql(value));
    }

    public static String addConstraintMinMaxDate(String beanAbrev, String atributeName, Date valueMin, Date valueMax) {
        return addConstraintMinMax(beanAbrev, atributeName, DateUtil.convertFormUtilToSql(valueMin), DateUtil.convertFormUtilToSql(valueMax));
    }

    public static String supprimerCleEtranger(String beanAbrev, String atributeName, String condition, Object value) {
        String requet = "";
        if (value != null) {
            requet = "UPDATE " + beanAbrev + " SET " + atributeName + " = NULL WHERE " + condition + " = " + value;
        }
        return requet;
    }

    public static String isTaxPaid(String abreviationAnnuel, String beanTrim, String abreviationNumTrim, int annee, int trim) {
        String requet = "";
        if (!abreviationAnnuel.equals("") && !beanTrim.equals("") && !abreviationNumTrim.equals("")) {
            requet = "SELECT item FROM " + beanTrim + " item WHERE item." + abreviationAnnuel + ".annee=" + annee + " AND item." + abreviationNumTrim + "=" + trim;
        }
        return requet;
    }

    // méthode pour ajouter une liste des contraintes dans une requete
    public static String findByAllString(String abreviationBean, List<String> attributes, List<String> data) {
        String requete = "";
        if (!abreviationBean.equals("") && !attributes.isEmpty() && attributes.size() == data.size()) {
            for (int i = 0; i < attributes.size(); i++) {
                requete += addConstraint(abreviationBean, attributes.get(i), "=", data.get(i));
            }
        }
        return requete;
    }
    
    public static boolean isStringNullOrVide(String str){
        return (str==null || str.equals(""));
    }

}
