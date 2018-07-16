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

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class ChemicalComponentLayer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private LevelLayer layer;
    @ManyToOne
    private ChemicalComponent chemicalComponent;
    private Double teneur;

    public ChemicalComponentLayer() {
    }

    public ChemicalComponentLayer(Long id) {
        this.id = id;
    }

    public ChemicalComponentLayer(LevelLayer layer, ChemicalComponent chemicalComponent, Double teneur) {
        this.layer = layer;
        this.chemicalComponent = chemicalComponent;
        this.teneur = teneur;
    }

    public ChemicalComponentLayer(Long id, LevelLayer layer, ChemicalComponent chemicalComponent, Double teneur) {
        this.id = id;
        this.layer = layer;
        this.chemicalComponent = chemicalComponent;
        this.teneur = teneur;
    }

    public LevelLayer getLevelLayer() {
        if(layer==null){
            layer = new LevelLayer();
        }
        return layer;
    }

    public void setLevelLayer(LevelLayer layer) {
        this.layer = layer;
    }

    public ChemicalComponent getChemicalComponent() {
        if(chemicalComponent==null){
            chemicalComponent = new ChemicalComponent();
        }
        return chemicalComponent;
    }

    public void setChemicalComponent(ChemicalComponent chemicalComponent) {
        this.chemicalComponent = chemicalComponent;
    }

    public Double getTeneur() {
        return teneur;
    }

    public void setTeneur(Double teneur) {
        this.teneur = teneur;
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
        if (!(object instanceof ChemicalComponentLayer)) {
            return false;
        }
        ChemicalComponentLayer other = (ChemicalComponentLayer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChemicalComponentLayer{" + "id=" + id + ", layer=" + getLevelLayer().getNom() + ", chemicalComponent=" + getChemicalComponent().getId() + ", teneur=" + teneur + '}';
    }

    
}
