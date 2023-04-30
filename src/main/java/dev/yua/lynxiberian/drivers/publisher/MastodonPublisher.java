package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.Post;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class MastodonPublisher extends PublishDriver {

    private OkHttpClient client;

    private String domain;
    private String token;

    @Override
    public String getName() {
        return "mastodon";
    }

    @Override
    public void onLoad() {
        this.client = new OkHttpClient();
        if(System.getenv("MASTODON_DOMAIN") != null){
            this.setReady(true);
        }
        this.domain = System.getenv("MASTODON_DOMAIN");
        this.token = System.getenv("MASTODON_TOKEN");
    }

    @Override
    public void publish(Post post) {
        long mediaId = this.uploadMedia(new File(post.getPath()));
        this.createStatus(post.getCaption(), mediaId);
    }

    public void createStatus(String caption, long mediaId){
        FormBody.Builder requestBuilder = new FormBody.Builder();
        if(caption != null && !caption.isEmpty()){
            requestBuilder.add("status", caption);
        }
        requestBuilder.add("media_ids[]", String.valueOf(mediaId));

        this.post("/api/v1/statuses", requestBuilder.build()).close();
    }


    public long uploadMedia(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "fox.jpg",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                        file))
                .build();

        try (Response response = this.post("/api/v2/media", requestBody)) {
            return new JSONObject(response.body().string()).getLong("id");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Response post(String endpoint, RequestBody requestBody){
        Request request = new Request.Builder()
                .url("https://"+this.domain+endpoint)
                .addHeader("Authorization", "Bearer " + this.token)
                .post(requestBody)
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
