package dev.yua.lynxiberian.drivers.publisher;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.Post;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TumblrPublisher extends PublishDriver {

    private JumblrClient client;
    private List<String> defaultTags;

    @Override
    public String getName() {
        return "tumblr";
    }

    @Override
    public void onLoad() {
        if(System.getenv("TUMBLR_CONSUMER_KEY") != null) {
            this.client = new JumblrClient(System.getenv("TUMBLR_CONSUMER_KEY"), System.getenv("TUMBLR_CONSUMER_SECRET"));
            this.client.setToken(System.getenv("TUMBLR_TOKEN_KEY"), System.getenv("TUMBLR_TOKEN_SECRET"));
            String defaultTags = System.getenv("TUMBLR_TAGS");
            if(defaultTags == null){
                this.defaultTags = new ArrayList<>();
            }else{
                this.defaultTags = Arrays.asList(defaultTags.split(" "));
            }

            this.setReady(true);
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
            if(post.getSource() != null){
                tumblrPost.setLinkUrl(post.getSource());
            }

            tumblrPost.setTags(this.defaultTags);

            tumblrPost.save();
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
