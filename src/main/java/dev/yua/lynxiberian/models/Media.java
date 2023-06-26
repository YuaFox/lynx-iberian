package dev.yua.lynxiberian.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caption;
    private String path;

    private String source;

    @ManyToOne
    @JsonIgnore
    @JoinTable(name = "bucket_media",
            joinColumns           = @JoinColumn(name = "media_id",     referencedColumnName = "id"),
            inverseJoinColumns    = @JoinColumn(name = "bucket_id",      referencedColumnName = "id"))
    private Bucket bucket;

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

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
