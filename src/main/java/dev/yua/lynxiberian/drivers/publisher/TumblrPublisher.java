package dev.yua.lynxiberian.drivers.publisher;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.responses.ResponseWrapper;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.Post;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

            tumblrPost.setTags(this.defaultTags);

            if(post.getSource() != null){
                tumblrPost.setCaption(String.format("<a href='%s'>Source</a>", post.getSource()));
            }

            tumblrPost.save();


        } catch (IllegalAccessException | InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
