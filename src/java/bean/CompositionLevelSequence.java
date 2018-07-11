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
public class CompositionLevelSequence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private LevelLayer level;
    @ManyToOne
    private SequenceLevel sequenceLevel;
    private Boolean existe;

    public CompositionLevelSequence() {
    }

    public CompositionLevelSequence(Long id) {
        this.id = id;
    }

    public CompositionLevelSequence(Long id, LevelLayer level, SequenceLevel sequenceLevel, Boolean existe) {
        this.id = id;
        this.level = level;
        this.sequenceLevel = sequenceLevel;
        this.existe = existe;
    }

    public CompositionLevelSequence(LevelLayer level, SequenceLevel sequenceLevel, Boolean existe) {
        this.level = level;
        this.sequenceLevel = sequenceLevel;
        this.existe = existe;
    }

    public LevelLayer getLevel() {
        if(level==null){
            level= new LevelLayer();
        }
        return level;
    }

    public void setLevel(LevelLayer level) {
        this.level = level;
    }

    public SequenceLevel getSequenceLevel() {
        if(sequenceLevel==null){
            sequenceLevel= new SequenceLevel();
        }
        return sequenceLevel;
    }

    public void setSequenceLevel(SequenceLevel sequenceLevel) {
        this.sequenceLevel = sequenceLevel;
    }

    public Boolean getExiste() {
        return existe;
    }

    public void setExiste(Boolean existe) {
        this.existe = existe;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof CompositionLevelSequence)) {
            return false;
        }
        CompositionLevelSequence other = (CompositionLevelSequence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CompositionLevelSequence{" + "id=" + id + ", level=" + getLevel().getNom() + ", sequenceLevel=" + getSequenceLevel().getId() + ", existe=" + existe + '}';
    }

    
}
