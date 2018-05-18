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
public class Treatment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Machine machine;
    @ManyToOne
    private Operation operation;
    @ManyToOne
    private Block block;
    private Long temps;
    @ManyToOne
    private UnitOfTime unitOfTime;
    private Long chronologicalOrder;

    public Treatment() {
    }

    public Treatment(Machine machine, Operation operation, Block block, Long temps, Long chronologicalOrder) {
        this.machine = machine;
        this.operation = operation;
        this.block = block;
        this.temps = temps;
        this.chronologicalOrder = chronologicalOrder;
    }

    public Treatment(Machine machine, Operation operation, Block block, Long temps, UnitOfTime unitOfTime, Long chronologicalOrder) {
        this.machine = machine;
        this.operation = operation;
        this.block = block;
        this.temps = temps;
        this.unitOfTime = unitOfTime;
        this.chronologicalOrder = chronologicalOrder;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Long getTemps() {
        return temps;
    }

    public void setTemps(Long temps) {
        this.temps = temps;
    }

    public Long getChronologicalOrder() {
        return chronologicalOrder;
    }

    public void setChronologicalOrder(Long chronologicalOrder) {
        this.chronologicalOrder = chronologicalOrder;
    }

    public UnitOfTime getUnitOfTime() {
        return unitOfTime;
    }

    public void setUnitOfTime(UnitOfTime unitOfTime) {
        this.unitOfTime = unitOfTime;
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
        if (!(object instanceof Treatment)) {
            return false;
        }
        Treatment other = (Treatment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Treatment{" + "id=" + id + ", machine=" + getMachine().getNom() + ", operation=" + getOperation().getNom() + ", block=" + getBlock().getId() + ", temps=" + temps + ", unitOfTime=" + unitOfTime + ", chronologicalOrder=" + chronologicalOrder + '}';
    }

    

}
