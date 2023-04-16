package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.drivers.PublishDriver;

import java.io.*;
import java.util.List;

import dev.yua.lynxiberian.models.Post;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import io.github.redouane59.twitter.dto.tweet.ReplySettings;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.springframework.stereotype.Component;


@Component
public class TwitterPublisher extends PublishDriver {

    private TwitterClient twitterClient;

    @Override public String getName() { return "twitter"; }
    @Override
    public void onLoad() {
        String apiKey = System.getenv("TWITTER_API_KEY");
        String apiSecret = System.getenv("TWITTER_API_SECRET");
        String clientId = System.getenv("TWITTER_CLIENT_ID");
        String clientSecret = System.getenv("TWITTER_CLIENT_SECRET");
        if(apiKey != null && apiSecret != null && clientId != null && clientSecret != null){
            twitterClient = new TwitterClient(TwitterCredentials.builder()
                    .accessToken(clientId)
                    .accessTokenSecret(clientSecret)
                    .apiKey(apiKey)
                    .apiSecretKey(apiSecret)
                    .build());
            this.setReady(true);
        }
    }

    @Override
    public void publish(Post post) {
        String mediaId = null;

        if(post.getPath() != null)
            mediaId = twitterClient.uploadMedia(new File(post.getPath()), MediaCategory.TWEET_IMAGE).getMediaId();

        String caption = post.getCaption();
        if(caption == null) caption = "";

        if(mediaId != null) {
            twitterClient.postTweet(TweetParameters.builder().text(caption).media(
                    TweetParameters.Media.builder().mediaIds(List.of(mediaId)).build()
            ).build());
        }else{
            twitterClient.postTweet(TweetParameters.builder().text(caption).build());
        }
    }
}
