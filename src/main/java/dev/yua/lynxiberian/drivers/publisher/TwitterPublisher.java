package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.drivers.PublishDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import dev.yua.lynxiberian.models.Post;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import io.github.redouane59.twitter.dto.tweet.ReplySettings;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;


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

        if(post.getPath() != null) {
            try {
                BufferedImage originalImage = ImageIO.read(new File(post.getPath()));
                BufferedImage finalImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight() + 40, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = finalImage.createGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
                g.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
                g.setColor(Color.WHITE);
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                try(InputStream is = classloader.getResourceAsStream("static/fonts/UbuntuMono-Regular.ttf")){
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
                    g.setFont(customFont);
                    g.drawString(post.getSource(), 10, finalImage.getHeight()-14);
                }
                File f = File.createTempFile("foxes/", ".png");
                f.deleteOnExit();
                ImageIO.write(finalImage, "PNG", f);
                mediaId = twitterClient.uploadMedia(f, MediaCategory.TWEET_IMAGE).getMediaId();
            }catch (Exception e){
                e.printStackTrace();
                mediaId = twitterClient.uploadMedia(new File(post.getPath()), MediaCategory.TWEET_IMAGE).getMediaId();
            }
        }

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
