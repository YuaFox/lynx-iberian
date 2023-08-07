package dev.yua.lynxiberian.drivers.publisher;

import bsky4j.ATProtocolFactory;
import bsky4j.BlueskyFactory;
import bsky4j.api.entity.atproto.repo.RepoUploadBlobRequest;
import bsky4j.api.entity.atproto.repo.RepoUploadBlobResponse;
import bsky4j.api.entity.atproto.server.ServerCreateSessionRequest;
import bsky4j.api.entity.atproto.server.ServerCreateSessionResponse;
import bsky4j.api.entity.bsky.feed.FeedPostRequest;
import bsky4j.api.entity.bsky.feed.FeedPostResponse;
import bsky4j.api.entity.share.Response;
import bsky4j.domain.Service;
import bsky4j.model.bsky.embed.EmbedImages;
import bsky4j.model.bsky.embed.EmbedImagesImage;
import dev.yua.lynxiberian.bots.DiscordBot;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.DiscordServer;
import dev.yua.lynxiberian.models.Post;
import dev.yua.lynxiberian.repositories.DiscordServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlueskyPublisher extends PublishDriver {

    private String accessJwt = null;

    @Override
    public void onLoad() {
        if(System.getenv("BLUESKY_HANDLE") != null && System.getenv("BLUESKY_PASSWORD") != null){
            Response<ServerCreateSessionResponse> response = BlueskyFactory
                    .getInstance(Service.BSKY_SOCIAL.getUri())
                    .server().createSession(
                            ServerCreateSessionRequest.builder()
                                    .identifier(System.getenv("BLUESKY_HANDLE"))
                                    .password(System.getenv("BLUESKY_PASSWORD"))
                                    .build()
                    );

            accessJwt = response.get().getAccessJwt();
            if(accessJwt != null){
                this.setReady(true);
            }
        }
    }

    @Override
    public String getName() {
        return "bluesky";
    }

    @Override
    public void publish(Post post) {
        String caption = "";
        if(post.getCaption() != null) {
            caption += post.getCaption() + "\n";
        }

        if(post.getAuthor() != null) {
            caption += "\uD83D\uDCF8 " +post.getAuthor() + "\n";
        }else{
            caption += "\uD83D\uDCF8 Unknown" + "\n";
        }

        if(post.getUrl() != null) {
            caption += "\uD83D\uDD17 " +post.getUrl() + "\n";
        }

        try (InputStream stream = new FileInputStream(post.getPath())) {
            Response<RepoUploadBlobResponse> response1 = ATProtocolFactory
                    .getInstance(Service.BSKY_SOCIAL.getUri())
                    .repo().uploadBlob(
                            RepoUploadBlobRequest.fromStreamBuilder()
                                    .accessJwt(accessJwt)
                                    .stream(stream)
                                    .name("image.png")
                                    .build()
                    );

            EmbedImages imagesMain = new EmbedImages();
            {
                List<EmbedImagesImage> images = new ArrayList<>();
                imagesMain.setImages(images);

                EmbedImagesImage image = new EmbedImagesImage();
                image.setImage(response1.get().getBlob());
                image.setAlt("A fox!");
                images.add(image);
            }

            Response<FeedPostResponse> response2 = BlueskyFactory
                    .getInstance(Service.BSKY_SOCIAL.getUri())
                    .feed().post(
                            FeedPostRequest.builder()
                                    .accessJwt(accessJwt)
                                    .text(caption)
                                    .embed(imagesMain)
                                    .build()
                    );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

