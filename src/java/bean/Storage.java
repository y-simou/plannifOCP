/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    private Machine machine;
    @ManyToOne
    private Block block;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateTransport;
    private BigDecimal quantity;

    public Storage() {
    }

    public Storage(Stock stock, Machine machine, Block block, Date dateTransport, BigDecimal quantity) {
        this.stock = stock;
        this.machine = machine;
        this.block = block;
        this.dateTransport = dateTransport;
        this.quantity = quantity;
    }

    public Stock getStock() {
        if (stock == null) {
            stock = new Stock();
        }
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Machine getMachine() {
        if (machine == null) {
            machine = new Machine();
        }
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Block getBlock() {
        if (block == null) {
            block = new Block();
        }
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Date getDateTransport() {
        return dateTransport;
    }

    public void setDateTransport(Date dateTransport) {
        this.dateTransport = dateTransport;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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
        if (!(object instanceof Storage)) {
            return false;
        }
        Storage other = (Storage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Storage{" + "id=" + id + ", stock=" + getStock().getNom() + ", machine=" + getMachine().getNom() + ", block=" + getBlock().getId() + ", dateTransport=" + dateTransport + ", quantity=" + quantity + '}';
    }

}
