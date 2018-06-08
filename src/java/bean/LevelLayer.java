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
import javax.persistence.ManyToOne;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class LevelLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int num;
    private String nom;
    @ManyToOne
    private Parcel parcel;

    public LevelLayer() {
    }

    public LevelLayer(int num, String nom) {
        this.num = num;
        this.nom = nom;
    }

    public LevelLayer(Long id, Parcel parcel) {
        this.id = id;
        this.parcel = parcel;
    }
    
    public LevelLayer(int num, Parcel parcel) {
        this.num = num;
        this.parcel = parcel;
    }

    public LevelLayer(Long id, int num, Parcel parcel) {
        this.id = id;
        this.num = num;
        this.parcel = parcel;
    }
    
    public LevelLayer(int num, String nom, Parcel parcel) {
        this.num = num;
        this.nom = nom;
        this.parcel = parcel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Parcel getParcel() {
        if (parcel == null) {
            parcel = new Parcel();
        }
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LevelLayer)) {
            return false;
        }
        LevelLayer other = (LevelLayer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "num= " + num + ", nom= " + nom + ", parcel= " + parcel.getNom() ;
    }

}
