/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
@XmlRootElement
public class Panel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Long surface;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePDebutExploitation;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePFinExploitation;
    private Long reserves;
    private Long x;
    private Long y;

    public Panel() {
    }

    public Panel(String nom) {
        this.nom = nom;
    }

    public Panel(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Panel(String nom, Long surface, Date datePDebutExploitation, Date datePFinExploitation, Long reserves, Long x, Long y) {
        this.nom = nom;
        this.surface = surface;
        this.datePDebutExploitation = datePDebutExploitation;
        this.datePFinExploitation = datePFinExploitation;
        this.reserves = reserves;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    public Long getSurface() {
        return surface;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
    }

    public Date getDatePDebutExploitation() {
        return datePDebutExploitation;
    }

    public void setDatePDebutExploitation(Date datePDebutExploitation) {
        this.datePDebutExploitation = datePDebutExploitation;
    }

    public Date getDatePFinExploitation() {
        return datePFinExploitation;
    }

    public void setDatePFinExploitation(Date datePFinExploitation) {
        this.datePFinExploitation = datePFinExploitation;
    }

    public Long getReserves() {
        return reserves;
    }

    public void setReserves(Long reserves) {
        this.reserves = reserves;
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


    

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Panel other = (Panel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Panel{" + "id=" + id + ", nom=" + nom + ", surface=" + surface + ", datePDebutExploitation=" + datePDebutExploitation + ", datePFinExploitation=" + datePFinExploitation + ", reserves=" + reserves + ", x=" + x + ", y=" + y + '}';
    }

    
}
