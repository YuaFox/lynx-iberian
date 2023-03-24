package dev.yua.lynxiberian.utils.reddit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RedditApi {

    private static JSONObject call(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "linux:lynx-iberian:2023.4 (by /u/yuafox)")
                .get()
                .build();

        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            return new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JSONObject getPosts(String subreddit, String afterFullName) {
        JSONObject data = afterFullName != null ?
                RedditApi.call("https://www.reddit.com/r/"+subreddit+"/new.json?limit=100&after="+afterFullName) :
                RedditApi.call("https://www.reddit.com/r/"+subreddit+"/new.json?limit=100");
        return data.getJSONObject("data");
    }
}