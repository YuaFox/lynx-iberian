package dev.yua.lynxiberian.controllers;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.models.TwitterAccount;
import dev.yua.lynxiberian.repositories.TwitterAccountRepository;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.others.RequestToken;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts/twitter")
public class TwitterController {

    private final String consumerKey = System.getenv("TWITTER_KEY");
    private final String secretKey = System.getenv("TWITTER_SECRET");
    private TwitterClient twitterClient;
    private RequestToken requestToken;

    @Autowired
    private TwitterAccountRepository twitterAccountRepository;

    @PostMapping({"/createLogin"})
    public String createLink() {
        this.twitterClient = new TwitterClient(TwitterCredentials.builder()
                .accessToken("")
                .accessTokenSecret("")
                .apiKey(consumerKey)
                .apiSecretKey(secretKey)
                .build());

        this.twitterClient.getTwitterCredentials().setAccessToken("");
        this.twitterClient.getTwitterCredentials().setAccessTokenSecret("");
        this.requestToken = this.twitterClient.getOauth1Token("oob");
        return "https://twitter.com/oauth/authenticate?oauth_token=" + requestToken.getOauthToken();
    }

    @PostMapping(value = "/confirmLogin", produces = "application/json")
    public boolean confirmLogin(@RequestParam String pin) {
        System.out.println("Pin: "+pin);
        RequestToken oAuth1AccessToken = twitterClient.getOAuth1AccessToken(requestToken, pin);

        twitterClient.setTwitterCredentials(
                TwitterCredentials.builder()
                        .accessToken(oAuth1AccessToken.getOauthToken())
                        .accessTokenSecret(oAuth1AccessToken.getOauthTokenSecret())
                        .build()
        );

        TwitterAccount account = new TwitterAccount();
        account.setToken(oAuth1AccessToken.getOauthToken());
        account.setSecret(oAuth1AccessToken.getOauthTokenSecret());
        twitterAccountRepository.save(account);
        return true;
    }
}
