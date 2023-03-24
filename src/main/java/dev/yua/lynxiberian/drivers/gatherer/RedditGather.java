package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.drivers.GatherResults;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.models.entity.Filter;
import dev.yua.lynxiberian.models.entity.RedditFilter;
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
    public void gather(List<Filter> filters, GatherResults results) {
        List<Filter> redditFilters = filters.stream().filter(f -> f instanceof RedditFilter).toList();
        if(redditFilters.size() == 0) return;

        System.out.println("Starting reddit gather...");
        System.out.println(redditFilters.size() + "filters found.");
        for(Filter filter : redditFilters){
            RedditFilter redditFilter = (RedditFilter) filter;

            System.out.println("Starting filter for r/"+redditFilter.getSubreddit());

            String after = null;
            int postAmount = -1;
            while(postAmount != 0){
                System.out.println("Starting lot");
                JSONObject listing = RedditApi.getPosts(redditFilter.getSubreddit(), after);
                after = listing.isNull("after") ? null : listing.getString("after");
                JSONArray posts = listing.getJSONArray("children");
                postAmount = posts.length();

                System.out.println("Got "+postAmount+" posts.");
                results.setMediaTotal(results.getMediaTotal() + postAmount);

                if(postAmount == 0) continue;

                for(int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i).getJSONObject("data");
                    this.save("reddit", post, List.of(redditFilter), results);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        System.out.println("Done reddit gather.");

    }
}
