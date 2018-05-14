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
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Long puissance;
    private Long volume;
    private int priority;
    @ManyToOne
    private Parcel parcel;
    @ManyToOne
    private LevelLayer level;

    public Block() {
    }

    public Block(String nom, Long puissance, Long volume, int priority, Parcel parcel, LevelLayer level) {
        this.nom = nom;
        this.puissance = puissance;
        this.volume = volume;
        this.priority = priority;
        this.parcel = parcel;
        this.level = level;
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
    

    public Long getPuissance() {
        return puissance;
    }

    public void setPuissance(Long puissance) {
        this.puissance = puissance;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Parcel getParcel() {
        if(parcel==null){
            parcel = new Parcel();
        }
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public LevelLayer getLevel() {
        if(level== null){
            level =new LevelLayer();
        }
        return level;
    }

    public void setLevel(LevelLayer level) {
        this.level = level;
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
        if (!(object instanceof Block)) {
            return false;
        }
        Block other = (Block) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Block{" + "id=" + id + ", nom=" + nom + ", puissance=" + puissance + ", volume=" + volume + ", priority=" + priority + ", parcel=" + getParcel().getNom() + ", level=" + getLevel().getNom() + '}';
    }


    
    
}
