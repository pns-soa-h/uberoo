package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by Alexis Couvreur on 9/24/2018.
 */
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String label;

    public Tag() { this.label = ""; }

    public Tag(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(label, tag.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
