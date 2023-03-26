package dev.yua.lynxiberian.models.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.DiscriminatorOptions;

@Entity
@Table(name="media")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="media_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("null")
@DiscriminatorOptions(force = true)
public class Media implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption;
    private String path;
    private String meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    @Override
    public Media clone() {
        try {
            Media clone = (Media) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
