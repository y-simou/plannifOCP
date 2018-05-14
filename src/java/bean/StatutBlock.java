/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
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
public class StatutBlock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Block block;
    @ManyToOne
    private Statut statutInitial;
    @ManyToOne
    private Statut statutFinal;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateChangement;

    public StatutBlock() {
    }

    public StatutBlock(Long id) {
        this.id = id;
    }

    public StatutBlock(Block block, Statut statutInitial, Statut statutFinal, Date dateChangement) {
        this.block = block;
        this.statutInitial = statutInitial;
        this.statutFinal = statutFinal;
        this.dateChangement = dateChangement;
    }

    public Block getBlock() {
        if(block==null){
            block= new Block();
        }
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Statut getStatutInitial() {
        if(statutInitial == null){
            statutInitial = new Statut();
        }
        return statutInitial;
    }

    public void setStatutInitial(Statut statutInitial) {
        this.statutInitial = statutInitial;
    }

    public Statut getStatutFinal() {
        if(statutFinal == null){
            statutFinal = new Statut();
        }
        return statutFinal;
    }

    public void setStatutFinal(Statut statutFinal) {
        this.statutFinal = statutFinal;
    }

    public Date getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(Date dateChangement) {
        this.dateChangement = dateChangement;
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
        if (!(object instanceof StatutBlock)) {
            return false;
        }
        StatutBlock other = (StatutBlock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StatutBlock{" + "id=" + id + ", block=" + getBlock().getId() + ", statutInitial=" + getStatutInitial().getNom() + ", statutFinal=" + getStatutFinal().getNom() + ", dateChangement=" + dateChangement + '}';
    }


    
}
