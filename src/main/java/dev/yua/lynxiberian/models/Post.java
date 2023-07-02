package dev.yua.lynxiberian.models;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue("post")
public class Post extends Media {

    private long mediaId;

    public Post(){

    }

    public Post(Media media){
        this.setPath(media.getPath());
        this.setCaption(media.getCaption());
        this.setSource(media.getSource());
        this.setMediaId(media.getId());
    }

    @Transient
    private Map<String, Object> metadata;


    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public void setUrl(String url){
        this.setSource(url);
    }

    public String getUrl(){
        return this.getSource();
    }

    public void setDescription(String description){
        if(this.getMetadata() == null)
            this.setMetadata(new HashMap<>());
        this.getMetadata().put("description", description);
    }


    public String getDescription(){
        if(this.getMetadata() == null || !this.getMetadata().containsKey("description"))
            return null;
        return (String) this.getMetadata().get("description");
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public String getApiEndpoint(String extra){
        return System.getenv("APP_URL")+"/api/v1/media/"+this.getMediaId()+extra;
    }
}
