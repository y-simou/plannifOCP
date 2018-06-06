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

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String designation;
    private String type;
    private BigDecimal rendement;
    @ManyToOne
    private Site site;

    public Machine() {
    }

    public Machine(String id) {
        this.nom = id;
    }

    public Machine(String id, String designation, String type, BigDecimal rendement) {
        this.nom = id;
        this.designation = designation;
        this.type = type;
        this.rendement = rendement;
    }

    public Machine(String nom, String designation, String type, BigDecimal rendement, Site site) {
        this.nom = nom;
        this.designation = designation;
        this.type = type;
        this.rendement = rendement;
        this.site = site;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getRendement() {
        return rendement;
    }

    public void setRendement(BigDecimal rendement) {
        this.rendement = rendement;
    }

    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Machine other = (Machine) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Machine{" + "id=" + id + ", nom=" + nom + ", designation=" + designation + ", type=" + type + ", rendement=" + rendement + ", site=" + site + '}';
    }

    
}
