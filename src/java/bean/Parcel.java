/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
@XmlRootElement
public class Parcel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Long x;
    private Long y;
    private Long surface;
    private Long epaisseur;
    private String statut;
    private BigDecimal treatment;
    @ManyToOne
    private Trench trench;
    @ManyToOne
    private SubPanel subPanel;

    public Parcel() {
    }

    public Parcel(String nom) {
        this.nom = nom;
    }

    public Parcel(Long id, String nom, Trench trench) {
        this.id = id;
        this.nom = nom;
        this.trench = trench;
    }

    public Parcel(String nom, Trench trench) {
        this.nom = nom;
        this.trench = trench;
    }

    public Parcel(Long id, String nom, Trench trench, SubPanel subPanel) {
        this.id = id;
        this.nom = nom;
        this.trench = trench;
        this.subPanel = subPanel;
    }
    

    public Parcel(String nom, Long x, Long y, Trench trench, SubPanel subPanel) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.trench = trench;
        this.subPanel = subPanel;
    }

    public Parcel(String nom, Long x, Long y, Long surface, Long epaisseur, Trench trench, SubPanel subPanel) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.surface = surface;
        this.epaisseur = epaisseur;
        this.trench = trench;
        this.subPanel = subPanel;
    }

    public Parcel(String nom, Long x, Long y, Long surface, Long epaisseur, String statut, BigDecimal treatment, Trench trench, SubPanel subPanel) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.surface = surface;
        this.epaisseur = epaisseur;
        this.statut = statut;
        this.treatment = treatment;
        this.trench = trench;
        this.subPanel = subPanel;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Trench getTrench() {
        if (trench == null) {
            trench = new Trench();
        }
        return trench;
    }

    public void setTrench(Trench trench) {
        this.trench = trench;
    }

    public SubPanel getSubPanel() {
        if (subPanel == null) {
            subPanel = new SubPanel();
        }
        return subPanel;
    }

    public void setSubPanel(SubPanel subPanel) {
        this.subPanel = subPanel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getSurface() {
        return surface;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
    }

    public Long getEpaisseur() {
        return epaisseur;
    }

    public void setEpaisseur(Long epaisseur) {
        this.epaisseur = epaisseur;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Parcel other = (Parcel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Parcel{" + "id=" + id + ", nom=" + nom + ", x=" + x + ", y=" + y + ", surface=" + surface + ", epaisseur=" + epaisseur + ", statut=" + statut + ", treatment=" + treatment + ", trench=" + trench + ", subPanel=" + subPanel + '}';
    }
    
}
