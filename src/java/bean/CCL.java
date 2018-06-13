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
public class CCL implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @ManyToOne
    private SubPanel subPanel;
    private Long surface;
    

    public SubPanel getSubPanel() {
        if(subPanel==null){
           subPanel = new SubPanel();
        }
        return subPanel;
    }

    public CCL(String nom, SubPanel subPanel, Long surface) {
        this.nom = nom;
        this.subPanel = subPanel;
        this.surface = surface;
    }

    

    public CCL() {
    }
    

    public void setSubPanel(SubPanel subPanel) {
        this.subPanel = subPanel;
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


    public Long getSurface() {
        return surface;
    }

    public void setSurface(Long surface) {
        this.surface = surface;
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
        if (!(object instanceof CCL)) {
            return false;
        }
        CCL other = (CCL) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CCL{" + "id=" + id + ", nom=" + nom + ", subPanel=" + subPanel.getNom() + ", surface=" + surface + '}';
    }

    

    
}
