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
public class PositionMachine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Machine machine;
    @ManyToOne
    private Position position;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePosition;

    public PositionMachine() {
    }

    public PositionMachine(Machine machine, Position position, Date datePosition) {
        this.machine = machine;
        this.position = position;
        this.datePosition = datePosition;
    }

    public PositionMachine(Machine machine, Position position) {
        this.machine = machine;
        this.position = position;
        this.datePosition = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getDatePosition() {
        return datePosition;
    }

    public void setDatePosition(Date datePosition) {
        this.datePosition = datePosition;
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
        if (!(object instanceof PositionMachine)) {
            return false;
        }
        PositionMachine other = (PositionMachine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PositionMachine{" + "id=" + id + ", machine=" + machine + ", position=" + position.getNom() + ", datePosition=" + datePosition + '}';
    }

    
    
}
