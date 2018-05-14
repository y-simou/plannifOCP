/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author Yassine.SIMOU
 */
@Entity
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Long x;
    private Long y;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePosition;

    public Position() {
    }

    public Position(String nom) {
        this.nom = nom;
    }

    public Position(String nom, Long x, Long y, Date datePosition) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.datePosition = datePosition;
    }

    public Position(String nom, Long x, Long y) {
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.datePosition = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Date getDatePosition() {
        return datePosition;
    }

    public void setDatePosition(Date datePosition) {
        this.datePosition = datePosition;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Position{" + "id=" + id + ", nom=" + nom + ", x=" + x + ", y=" + y + ", datePosition=" + datePosition + '}';
    }

}
