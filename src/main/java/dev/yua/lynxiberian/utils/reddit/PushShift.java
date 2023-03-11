package dev.yua.lynxiberian.utils.reddit;

import dev.yua.lynxiberian.models.entity.RedditMedia;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class PushShift {

    private static JSONObject call(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JSONArray getPosts(String subreddit, long start, long end) {
        JSONObject data = PushShift.call("https://api.pushshift.io/reddit/search/submission/?subreddit="+subreddit+"&sort_type=created_utc&before="+end+"&after="+start+"&size=10000");
        return data.getJSONArray("data");
    }
}
