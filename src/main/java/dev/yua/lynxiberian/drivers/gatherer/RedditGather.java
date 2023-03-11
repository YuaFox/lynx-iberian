package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.models.entity.Filter;
import dev.yua.lynxiberian.models.entity.Media;
import dev.yua.lynxiberian.models.entity.RedditFilter;
import dev.yua.lynxiberian.utils.reddit.PushShift;
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
    public void gather(List<Filter> filters) {
        List<Filter> redditFilters = filters.stream().filter(f -> f instanceof RedditFilter).toList();
        if(redditFilters.size() != 1) return;
        RedditFilter filter = (RedditFilter) redditFilters.get(0);

        JSONArray posts = PushShift.getPosts(filter.getSubreddit(), filter.getStartTime(), filter.getEndTime());

        if(posts.length() == 0) return;

        for(int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            this.save("reddit", post, filters);
        }
    }
}
