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
import javax.persistence.ManyToOne;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class SubPanel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String surface;
    @ManyToOne
    private SequenceLevel sequenceLevel;

    public SubPanel() {
    }

    public SubPanel(String nom) {
        this.nom = nom;
    }

    public SubPanel(String nom, String surface, SequenceLevel sequenceLevel) {
        this.nom = nom;
        this.surface = surface;
        this.sequenceLevel = sequenceLevel;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public SequenceLevel getSequenceLevel() {
        if (sequenceLevel == null) {
            sequenceLevel = new SequenceLevel();
        }
        return sequenceLevel;
    }

    public void setSequenceLevel(SequenceLevel sequenceLevel) {
        this.sequenceLevel = sequenceLevel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final SubPanel other = (SubPanel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

   
    

    @Override
    public String toString() {
        return "SubPanel{" + "id=" + id + ", nom=" + nom + ", surface=" + surface + ", sequenceLevel=" + getSequenceLevel().getId() + '}';
    }


    
}
