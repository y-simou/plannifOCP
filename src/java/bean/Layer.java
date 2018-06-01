/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class Layer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @OneToOne
    private LevelLayer level;

    public Layer() {
    }

    public Layer(String nom, LevelLayer level) {
        this.nom = nom;
        this.level = level;
    }

    public Layer(Long id, String nom, LevelLayer level) {
        this.id = id;
        this.nom = nom;
        this.level = level;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LevelLayer getLevel() {
        if (level == null) {
            level = new LevelLayer();
        }
        return level;
    }

    public void setLevel(LevelLayer level) {
        this.level = level;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final Layer other = (Layer) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "Couche{" + "id=" + id + ", id=" + nom + ", level=" + getLevel().getNum() + '}';
    }

}
