package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.drivers.PublishDriver;

import java.io.*;
import java.util.List;

import dev.yua.lynxiberian.models.Post;
import dev.yua.lynxiberian.models.TwitterAccount;
import dev.yua.lynxiberian.repositories.TwitterAccountRepository;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TwitterPublisher extends PublishDriver {

    @Autowired
    private TwitterAccountRepository twitterAccountRepository;

    @Override public String getName() { return "twitter"; }
    @Override
    public void onLoad() {
    }

    @Override
    public void publish(Post post) {
        String consumerKey = System.getenv("TWITTER_KEY");
        String secretKey = System.getenv("TWITTER_SECRET");

        TwitterAccount account = twitterAccountRepository.findById(1L).get();

        TwitterClient twitterClient = new TwitterClient(TwitterCredentials.builder()
                .accessToken(account.getToken())
                .accessTokenSecret(account.getSecret())
                .apiKey(consumerKey)
                .apiSecretKey(secretKey)
                .build());

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
