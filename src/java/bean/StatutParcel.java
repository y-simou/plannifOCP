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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class StatutParcel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Parcel parcel;
    private String nom;
    private Long treatment;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateTreatment;

    public StatutParcel() {
    }

    public StatutParcel(String nom, Long treatment) {
        this.nom = nom;
        this.treatment = treatment;
    }

    public StatutParcel(Parcel parcel, String nom, Long treatment) {
        this.parcel = parcel;
        this.nom = nom;
        this.treatment = treatment;
        this.dateTreatment=new Date();
    }

    public StatutParcel(Parcel parcel, String nom, Long treatment, Date dateTreatment) {
        this.parcel = parcel;
        this.nom = nom;
        this.treatment = treatment;
        this.dateTreatment = dateTreatment;
    }

    public Date getDateTreatment() {
        return dateTreatment;
    }

    public void setDateTreatment(Date dateTreatment) {
        this.dateTreatment = dateTreatment;
    }
    
    public StatutParcel(String nom) {
        this.nom = nom;
    }

    public Long getTreatment() {
        return treatment;
    }

    public void setTreatment(Long treatment) {
        this.treatment = treatment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final StatutParcel other = (StatutParcel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StatutParcel{" + "id=" + id + ", parcel=" + parcel.getNom() + ", nom=" + nom + ", treatment=" + treatment + ", dateTreatment=" + dateTreatment + '}';
    }


}
