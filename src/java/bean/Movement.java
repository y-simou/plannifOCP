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
public class Movement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Machine machine;
    @ManyToOne
    private Position depart;
    @ManyToOne
    private Position arrive;
    private Long duree;

    public Movement() {
    }

    public Movement(Long id) {
        this.id = id;
    }

    public Movement(Machine machine, Position depart, Position arrive, Long duree) {
        this.machine = machine;
        this.depart = depart;
        this.arrive = arrive;
        this.duree = duree;
    }

    public Machine getMachine() {
        if(machine==null){
            machine=new Machine();
        }
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Position getDepart() {
        if(depart==null){
            depart = new Position();
        }
        return depart;
    }

    public void setDepart(Position depart) {
        this.depart = depart;
    }

    public Position getArrive() {
        if(arrive==null){
            arrive = new Position();
        }
        return arrive;
    }

    public void setArrive(Position arrive) {
        this.arrive = arrive;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
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
        return "Movemennt{" + "id=" + id + ", machine=" + getMachine().getNom() + ", depart=" + getDepart().getNom() + ", arrive=" + getArrive().getNom() + ", duree=" + duree + '}';
    }

    
}
