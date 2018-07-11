/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class LevelLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int sequenceNiveau;
    private String nom;
    private BigDecimal puissance;
    private BigDecimal surface;
    private BigDecimal volume;
    private BigDecimal thc;
    private BigDecimal tauxrecup;
    private boolean phosphate;
    @ManyToOne
    private Parcel parcel;

    public LevelLayer() {
    }

    public LevelLayer(String nom, Parcel parcel) {
        this.nom = nom;
        this.parcel = parcel;
    }

    public LevelLayer(int sequenceNiveau, String nom, BigDecimal puissance, BigDecimal volume, BigDecimal thc, BigDecimal tauxrecup, boolean phosphate, Parcel parcel) {
        this.sequenceNiveau = sequenceNiveau;
        this.nom = nom;
        this.puissance = puissance;
        this.volume = volume;
        this.thc = thc;
        this.tauxrecup = tauxrecup;
        this.phosphate = phosphate;
        this.parcel = parcel;
    }

    public LevelLayer(int sequenceNiveau, String nom, BigDecimal puissance, BigDecimal surface, BigDecimal volume, BigDecimal thc, BigDecimal tauxrecup, boolean phosphate, Parcel parcel) {
        this.sequenceNiveau = sequenceNiveau;
        this.nom = nom;
        this.puissance = puissance;
        this.surface = surface;
        this.volume = volume;
        this.thc = thc;
        this.tauxrecup = tauxrecup;
        this.phosphate = phosphate;
        this.parcel = parcel;
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
        if (parcel == null) {
            parcel = new Parcel();
        }
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public int getSequenceNiveau() {
        return sequenceNiveau;
    }

    public void setSequenceNiveau(int sequenceNiveau) {
        this.sequenceNiveau = sequenceNiveau;
    }

    public BigDecimal getPuissance() {
        return puissance;
    }

    public void setPuissance(BigDecimal puissance) {
        this.puissance = puissance;
    }

    public BigDecimal getSurface() {
        return surface;
    }

    public void setSurface(BigDecimal surface) {
        this.surface = surface;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getThc() {
        return thc;
    }

    public void setThc(BigDecimal thc) {
        this.thc = thc;
    }

    public BigDecimal getTauxrecup() {
        return tauxrecup;
    }

    public void setTauxrecup(BigDecimal tauxrecup) {
        this.tauxrecup = tauxrecup;
    }

    public boolean isPhosphate() {
        return phosphate;
    }

    public void setPhosphate(boolean phosphate) {
        this.phosphate = phosphate;
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
        return "LevelLayer{" + "id=" + id + ", sequenceNiveau=" + sequenceNiveau + ", nom=" + nom + ", puissance=" + puissance + ", surface=" + surface + ", volume=" + volume + ", thc=" + thc + ", tauxrecup=" + tauxrecup + ", phosphate=" + phosphate + ", parcel=" + parcel + '}';
    }

    

}
