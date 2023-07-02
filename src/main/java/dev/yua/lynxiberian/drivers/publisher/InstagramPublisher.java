package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.bots.DiscordBot;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.DiscordServer;
import dev.yua.lynxiberian.models.Post;
import dev.yua.lynxiberian.repositories.DiscordServerRepository;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

@Component
public class InstagramPublisher extends PublishDriver {

    private OkHttpClient client;
    private String token;
    private String userId;

    @Override
    public void onLoad() {
        this.client = new OkHttpClient();
        if(System.getenv("INSTAGRAM_TOKEN") != null){
            this.setReady(true);
        }
        this.token = System.getenv("INSTAGRAM_TOKEN");
        this.userId = System.getenv("INSTAGRAM_USER");
    }

    @Override
    public String getName() {
        return "instagram";
    }

    @Override
    public boolean isReady() {
        return DiscordBot.getBot().enabled();
    }

    @Override
    public void publish(Post post) {
        try {
            // TODO: remove hardcoded limit
            int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if(hour != 17){
                return;
            }
            Response r = this.post(this.createMedia(post.getCaption(), post.getApiEndpoint("")));
            String id = new JSONObject(r.body().string()).getString("id");
            this.post(this.publishMedia(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HttpUrl createMedia(String caption, String path){
        HttpUrl.Builder httpBuilder = HttpUrl.parse("https://graph.facebook.com/v17.0/"+this.userId+"/media").newBuilder();
        httpBuilder.addQueryParameter("access_token",this.token);
        httpBuilder.addQueryParameter("caption",caption);
        httpBuilder.addQueryParameter("image_url",path);
        return httpBuilder.build();
    }

    public HttpUrl publishMedia(String id){
        HttpUrl.Builder httpBuilder = HttpUrl.parse("https://graph.facebook.com/v17.0/"+this.userId+"/media_publish").newBuilder();
        httpBuilder.addQueryParameter("access_token",this.token);
        httpBuilder.addQueryParameter("creation_id",id);
        return httpBuilder.build();
    }

    public Response post(HttpUrl url){
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
