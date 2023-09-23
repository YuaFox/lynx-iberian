package dev.yua.lynxiberian.drivers.gatherer;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.repositories.FlickrMediaRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Component
public class FlickrGather extends GathererDriver {
    private Flickr f;
    private File folderStorage;
    @Autowired
    public FlickrMediaRepository repository;

    @Override
    public String getName() {
        return "flickr";
    }

    @Override
    public void onLoad() {
        this.folderStorage = new File("storage/flickr");
        this.folderStorage.mkdirs();
        if(System.getenv("FLICKR_KEY") != null) {
            this.f = new Flickr(System.getenv("FLICKR_KEY"), System.getenv("FLICKR_SECRET"), new REST());
            this.setReady(true);
        }else{
            this.setReady(false);
        }
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
        SearchParameters searchParameters = new SearchParameters();
        //searchParameters.setText("fox");
        searchParameters.setGroupId("29106652@N00");
        searchParameters.setLicense(license);
        try {
            PhotoList<Photo> photos;
            for(int i = 0; !(photos = f.getPhotosInterface().search(searchParameters, 30, i)).isEmpty(); i++) {
                for (Photo p : photos) {
                    try {
                        if (repository.flickrId(p.getId()) == null) continue;

                        FlickrMedia media = new FlickrMedia();

                        media.setFlickrId(p.getId());
                        media.setSource(p.getUrl());
                        media.setPath(
                                LynxiberianApplication.http.download(
                                        p.getLargeUrl(), this.folderStorage.getPath() + "/" + FilenameUtils.getName(p.getLargeUrl())).getPath()
                        );
                        if (p.getOwner() != null) {
                            User info = f.getPeopleInterface().getInfo(p.getOwner().getId());
                            String author = Optional.ofNullable(info.getRealName()).orElse(info.getUsername());
                            media.setAuthor(author);
                        }

                        this.save(gatherRequest, media);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                Thread.sleep(2000);
            }
        } catch (FlickrException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

