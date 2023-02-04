package dev.yua.lynxiberian.models.entity;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@DiscriminatorValue("post")
public class Post extends Media {

    @Transient
    private Map<String, Object> metadata;


    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
