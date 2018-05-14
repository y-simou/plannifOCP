/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class LevelLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    private String nom;

    public LevelLayer() {
    }

    public LevelLayer(int num) {
        this.num = num;
    }

    public LevelLayer(String nom) {
        this.nom = nom;
    }

    public LevelLayer(int num, String nom) {
        this.num = num;
        this.nom = nom;
    }
    

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) num;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the num fields are not set
        if (!(object instanceof LevelLayer)) {
            return false;
        }
        LevelLayer other = (LevelLayer) object;
        if (this.num != other.num) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Level{" + "num=" + num + ", nom=" + nom + '}';
    }

    
}
