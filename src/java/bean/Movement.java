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
public class Movement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private PositionMachine depart;
    @ManyToOne
    private PositionMachine arrive;
    private BigDecimal duree;
    @ManyToOne
    private UnitOfTime unitOfTime;

    public Movement() {
    }

    public Movement(Long id) {
        this.id = id;
    }

    public Movement(PositionMachine depart, PositionMachine arrive, BigDecimal duree, UnitOfTime unitOfTime) {
        this.depart = depart;
        this.arrive = arrive;
        this.duree = duree;
        this.unitOfTime = unitOfTime;
    }

    public PositionMachine getDepart() {
        return depart;
    }

    public void setDepart(PositionMachine depart) {
        this.depart = depart;
    }

    public PositionMachine getArrive() {
        return arrive;
    }

    public void setArrive(PositionMachine arrive) {
        this.arrive = arrive;
    }

    public BigDecimal getDuree() {
        return duree;
    }

    public void setDuree(BigDecimal duree) {
        this.duree = duree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnitOfTime getUnitOfTime() {
        return unitOfTime;
    }

    public void setUnitOfTime(UnitOfTime unitOfTime) {
        this.unitOfTime = unitOfTime;
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
        if (!(object instanceof Movement)) {
            return false;
        }
        Movement other = (Movement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Movement{" + "id=" + id + ", depart=" + depart + ", arrive=" + arrive + ", duree=" + duree + '}';
    }

    
}
