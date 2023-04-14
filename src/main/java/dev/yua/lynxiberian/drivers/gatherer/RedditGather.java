package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.drivers.GatherMediaStatus;
import dev.yua.lynxiberian.models.GatherRequest;
import dev.yua.lynxiberian.models.GatherResult;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.models.Filter;
import dev.yua.lynxiberian.models.RedditFilter;
import dev.yua.lynxiberian.utils.reddit.RedditApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedditGather extends GathererDriver {
    @Override
    public String getName() {
        return "reddit";
    }

    @Override
    public void onLoad() {

    }

    @Override
    public GatherResult gather(GatherRequest gatherRequest) {
        GatherResult gatherResult = new GatherResult();

        if(!(gatherRequest.getFilter() instanceof RedditFilter redditFilter)) return gatherResult;

        String after = null;
        int postAmount = -1;
        int postDuplicated = 0;
        while(postAmount != 0 && postDuplicated < 3){
            JSONObject listing = RedditApi.getPosts(redditFilter.getSubreddit(), after);
            after = listing.isNull("after") ? null : listing.getString("after");
            JSONArray posts = listing.getJSONArray("children");
            postAmount = posts.length();

            gatherResult.setMediaTotal(gatherResult.getMediaTotal() + postAmount);

            if(postAmount == 0) continue;

            for(int i = 0; i < posts.length() && postDuplicated < 3; i++) {
                JSONObject post = posts.getJSONObject(i).getJSONObject("data");
                GatherMediaStatus mediaStatus = this.save("reddit", post, gatherRequest);
                if(mediaStatus == GatherMediaStatus.DUPLICATED) postDuplicated++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return gatherResult;
    }
}
