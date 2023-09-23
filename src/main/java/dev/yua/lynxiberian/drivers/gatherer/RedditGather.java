package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.repositories.RedditMediaRepository;
import dev.yua.lynxiberian.utils.Ffmpeg;
import dev.yua.lynxiberian.utils.reddit.RedditApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedditGather extends GathererDriver {

    @Autowired
    public RedditMediaRepository repository;
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

            if(postAmount == 0) continue;

            for(int i = 0; i < posts.length() && postDuplicated < 3; i++) {
                if(this.process(gatherRequest, posts.getJSONObject(i).getJSONObject("data"))){
                    postDuplicated++;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return gatherResult;
    }

    public boolean process(GatherRequest request, JSONObject post) {
        RedditMedia media = new RedditMedia();

        try {
            media.setSubreddit(post.getString("subreddit"));
            media.setPermalink(post.getString("id"));
            media.setAuthor(post.has("author") ? post.getString("author") : null);
            media.setTag(post.has("link_flair_text") && !post.isNull("link_flair_text") ? post.getString("link_flair_text").trim() : null);
            media.setTitle(post.has("title") ? post.getString("title") : "");

            String urlDest = post.has("url_overridden_by_dest") ? post.getString("url_overridden_by_dest") : null;
            String domain = post.has("domain") ? post.getString("domain") : null;

            // TODO: Detect crossposting properly
            boolean isImage = "i.redd.it".equals(domain)|| urlDest != null && urlDest.contains("i.redd.it");
            boolean isGallery = post.has("is_gallery") && post.getBoolean("is_gallery");
            boolean isVideo = post.has("is_video") && post.getBoolean("is_video") || urlDest != null && urlDest.contains("v.redd.it");
            boolean isRemoved = post.has("removed_by_category") && !post.isNull("removed_by_category");
            boolean isReddit = "v.redd.it".equals(domain) ||"i.redd.it".equals(domain) || "reddit.com".equals(domain) || (domain != null && domain.contains("/r/"));

            // Invalid post
            if(isRemoved || urlDest == null) return false;

            // Duplicated
            {
                List<RedditMedia> mediaList = repository.permalink(media.getPermalink());
                if (mediaList !=null && mediaList.size() > 0){
                    return true;
                }
            }

            // Not reddit
            if(!isReddit) return false;

            if(isVideo){
                String videoId = urlDest.replace("https://v.redd.it/", "");
                String videoUrl = null;
                String audioUrl = null;
                JSONObject videoData = LynxiberianApplication.http.getJsonObjectFromXml("https://v.redd.it/" + videoId + "/DASHPlaylist.mpd");
                Object videoDataContent = videoData.getJSONObject("MPD").getJSONObject("Period").get("AdaptationSet");
                if(videoDataContent instanceof JSONObject){
                    JSONArray representation = ((JSONObject) videoDataContent).getJSONArray("Representation");
                    int videoIndex = 0, maxHeight = 0;
                    for(int k = 0; k < representation.length(); k++){
                        int height = representation.getJSONObject(k).getInt("height");
                        if(height > maxHeight){
                            videoIndex = k;
                            maxHeight = height;
                        }
                    }
                    videoUrl = urlDest + "/" + representation.getJSONObject(videoIndex).getString("BaseURL");

                    String urlLocal = "storage/reddit/"+new URL(videoUrl).getFile().replace("/", ".").substring(1);
                    File local = LynxiberianApplication.http.download(videoUrl, urlLocal);
                    if(local.exists() && local.length() > 0){
                        media.setPath(urlLocal);
                        this.save(request, media);
                    }
                    return false;
                }
                if(videoDataContent instanceof JSONArray){
                    JSONArray videoDataContentArray = videoData.getJSONObject("MPD").getJSONObject("Period").getJSONArray("AdaptationSet");
                    for(int j = 0; j < videoDataContentArray.length(); j++){
                        JSONObject videoDataFile = videoDataContentArray.getJSONObject(j);
                        if("audio".equals(videoDataFile.get("contentType"))){
                            audioUrl = videoDataFile.getJSONObject("Representation").getString("BaseURL");
                        }else if("video".equals(videoDataFile.get("contentType"))){
                            JSONArray representation = videoDataFile.getJSONArray("Representation");
                            int videoIndex = 0, maxHeight = 0;
                            for(int k = 0; k < representation.length(); k++){
                                int height = representation.getJSONObject(k).getInt("height");
                                if(height > maxHeight){
                                    videoIndex = k;
                                    maxHeight = height;
                                }
                            }
                            videoUrl = representation.getJSONObject(videoIndex).getString("BaseURL");
                        }
                    }
                    videoUrl = urlDest + "/" + videoUrl;
                    audioUrl = urlDest + "/" + audioUrl;

                    String urlLocalVideo = "storage/reddit/"+new URL(videoUrl).getFile().replace("/", ".").substring(1);
                    File localVideo = LynxiberianApplication.http.download(videoUrl, urlLocalVideo);
                    String urlLocalAudio = "storage/reddit/"+new URL(audioUrl).getFile().replace("/", ".").substring(1);
                    File localAudio = LynxiberianApplication.http.download(audioUrl, urlLocalAudio);

                    if(localVideo.exists() && localVideo.length() > 0 && localAudio.exists() && localAudio.length() > 0){
                        Ffmpeg ffmpeg = new Ffmpeg();
                        File merged = ffmpeg.merge(localVideo, localAudio);
                        media.setPath(merged.getPath());
                        this.save(request, media);
                    }
                    return false;
                }
            }else if (isGallery) {
                boolean bruteforceImages = false;
                List<String> urlList = new ArrayList<>();
                JSONArray galleryData = post.getJSONObject("gallery_data").getJSONArray("items");
                JSONObject mediaMetadata = post.getJSONObject("media_metadata");

                for (int j = 0; j < galleryData.length(); j++) {
                    String mediaId = galleryData.getJSONObject(j).getString("media_id");

                    if(mediaMetadata.getJSONObject(mediaId).has("m")){
                        String mediaTypeG = mediaMetadata.getJSONObject(mediaId).getString("m").replace("image/", "");
                        urlList.add("https://i.redd.it/" + mediaId + "." + mediaTypeG);
                    }else{
                        urlList.add("https://i.redd.it/" + mediaId + ".jpg");
                        urlList.add("https://i.redd.it/" + mediaId + ".png");
                        urlList.add("https://i.redd.it/" + mediaId + ".webp");
                        urlList.add("https://i.redd.it/" + mediaId + ".gif");
                        bruteforceImages = true;
                    }
                }
                List<Media> mediaList = new ArrayList<>();
                for(String url : urlList){
                    RedditMedia submedia = media.clone();
                    String urlLocal = "storage/reddit/"+new URL(url).getFile().replace("/", ".").substring(1);
                    File local = LynxiberianApplication.http.download(url, urlLocal);
                    if(local.exists() && local.length() > 0){
                        submedia.setPath(urlLocal);
                        this.save(request, submedia);
                    }
                }
                return false;
            } else if(isImage) {
                String urlLocal = "storage/reddit/"+new URL(urlDest).getFile().replace("/", ".").substring(1);
                File local = LynxiberianApplication.http.download(urlDest, urlLocal);
                if(local.exists() && local.length() > 0){
                    media.setPath(urlLocal);
                    this.save(request, media);
                }
                return false;
            }

            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
