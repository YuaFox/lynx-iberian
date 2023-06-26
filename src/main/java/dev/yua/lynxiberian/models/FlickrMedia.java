package dev.yua.lynxiberian.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="media_flickr")
@DiscriminatorValue("flickr")
public class FlickrMedia extends Media implements Cloneable {

    private String flickrId;

    public String getFlickrId() {
        return flickrId;
    }

    public void setFlickrId(String id) {
        this.flickrId = id;
    }

    @Override
    public FlickrMedia clone() {
        FlickrMedia clone = (FlickrMedia) super.clone();
        // TODO: copy mutable state here, so the clone can't change the internals of the original
        return clone;
    }
}
