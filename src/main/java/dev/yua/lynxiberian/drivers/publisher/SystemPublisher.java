package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.entity.Post;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SystemPublisher extends PublishDriver {

    @Override
    public void onLoad() {

    }

    @Override
    public String getName() {
        return "system";
    }

    @Override
    public void publish(Post post) {
        System.out.println("---------- POST ----------");
        System.out.println("ID: "+post.getId());
        System.out.println("Text: "+post.getCaption());
        System.out.println("Media: "+post.getPath());
        System.out.println("Meta:");
        if(post.getMetadata() != null) {
            for (Map.Entry<String, Object> e : post.getMetadata().entrySet()) {
                System.out.println("  "+e.getKey() + " " + e.getValue());
            }
        }
    }
}
