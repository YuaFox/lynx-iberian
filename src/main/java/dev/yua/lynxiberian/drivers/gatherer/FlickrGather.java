package dev.yua.lynxiberian.drivers.gatherer;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.licenses.License;
import dev.yua.lynxiberian.drivers.GatherMediaStatus;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.models.GatherRequest;
import dev.yua.lynxiberian.models.GatherResult;
import dev.yua.lynxiberian.models.RedditFilter;
import dev.yua.lynxiberian.utils.reddit.RedditApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class FlickrGather extends GathererDriver {

    private Flickr f;

    @Override
    public String getName() {
        return "flickr";
    }

    @Override
    public void onLoad() {
        this.f = new Flickr(System.getenv("FLICKR_KEY"), System.getenv("FLICKR_SECRET"), new REST());
    }

    @Override
    public GatherResult gather(GatherRequest gatherRequest) {
        GatherResult gatherResult = new GatherResult();
        // 4: https://creativecommons.org/licenses/by/2.0/
        // 5: https://creativecommons.org/licenses/by-sa/2.0/
        // 6: https://creativecommons.org/licenses/by-nd/2.0/
        // 9 10 : Public domain

        this.search(gatherRequest, gatherResult, "4");
        this.search(gatherRequest, gatherResult, "5");
        this.search(gatherRequest, gatherResult, "6");
        this.search(gatherRequest, gatherResult, "9");
        this.search(gatherRequest, gatherResult, "10");

        return gatherResult;
    }

    private void search(GatherRequest gatherRequest, GatherResult gatherResult, String license){
        System.out.println("Find started!");
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setText("fox");
        searchParameters.setGroupId("29106652@N00");
        searchParameters.setLicense(license);
        int duplicatedAmount = 0;
        try {
            for(int i = 0; duplicatedAmount < 5; i++) {
                System.out.println("Finding set...");
                PhotoList<Photo> photos = f.getPhotosInterface().search(searchParameters, 30, i);
                if(photos.isEmpty()) {
                    System.out.println("Finished!");
                    break;
                }

                for (Photo p : photos) {
                    JSONObject object = new JSONObject();
                    object.put("id", p.getId());
                    object.put("source", p.getUrl());
                    object.put("media", p.getLargeUrl());
                    if(p.getOwner() != null) {
                        System.out.println(p.getOwner().getId());
                        User info = f.getPeopleInterface().getInfo(p.getOwner().getId());
                        String author = info.getRealName();
                        if(author == null)
                            author = info.getUsername();
                        System.out.println(author);
                        if(author != null && !author.isEmpty()) {
                            object.put("author",author);
                        }
                    }

                    GatherMediaStatus result = this.save("flickr", object, gatherRequest);
                    if(result == GatherMediaStatus.OK) {
                        System.out.println("Saved image");
                        gatherResult.addMediaAdded();
                    }
                    /*
                    if (result == GatherMediaStatus.DUPLICATED)
                        duplicatedAmount++;
                    if (duplicatedAmount == 5)
                        break;
                        *
                     */

                    Thread.sleep(2000);
                }
            }
        } catch (FlickrException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

