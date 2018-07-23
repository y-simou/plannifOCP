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
public class PanelSubPanel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Panel panel;
    @ManyToOne
    private SubPanel subpanel;

    public PanelSubPanel() {
    }

    public PanelSubPanel(Panel panel, SubPanel subpanel) {
        this.panel = panel;
        this.subpanel = subpanel;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public SubPanel getSubpanel() {
        return subpanel;
    }

    public void setSubpanel(SubPanel subpanel) {
        this.subpanel = subpanel;
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
        if (!(object instanceof PanelSubPanel)) {
            return false;
        }
        PanelSubPanel other = (PanelSubPanel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PanelSubPanel{" + "id=" + id + ", panel=" + panel + ", subpanel=" + subpanel + '}';
    }

}
