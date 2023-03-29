package dev.yua.lynxiberian.drivers.publisher;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.Post;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TumblrPublisher extends PublishDriver {

    JumblrClient client;

    @Override
    public String getName() {
        return "tumblr";
    }

    @Override
    public void onLoad() {
        if(System.getenv("TUMBLR_CONSUMER_KEY") != null) {
            this.client = new JumblrClient(System.getenv("TUMBLR_CONSUMER_KEY"), System.getenv("TUMBLR_CONSUMER_SECRET"));
            this.client.setToken(System.getenv("TUMBLR_TOKEN_KEY"), System.getenv("TUMBLR_TOKEN_SECRET"));
        }
    }

    @Override
    public void publish(Post post) {
        try {
            PhotoPost tumblrPost = client.newPost(System.getenv("TUMBLR_BLOG"), PhotoPost.class);
            tumblrPost.setCaption(post.getCaption());
            if(post.getPath().startsWith("http")){
                tumblrPost.setPhoto(new Photo(post.getPath()));
            }else {
                tumblrPost.setPhoto(new Photo(new File(post.getPath())));
            }
            if(post.getMetadata() != null) {
                if(post.getMetadata().containsKey("tags")) {
                    Object tags = post.getMetadata().get("tags");
                    if (tags instanceof List<?>) {
                        tumblrPost.setTags((List<String>) tags);
                    }
                }
                if(post.getMetadata().containsKey("source")) {
                    Object source = post.getMetadata().get("source");
                    if (source instanceof String) {
                        tumblrPost.setSource((String) source);
                    }
                }
            }
            tumblrPost.save();
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
