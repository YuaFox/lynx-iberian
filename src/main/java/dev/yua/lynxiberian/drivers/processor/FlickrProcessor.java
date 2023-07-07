package dev.yua.lynxiberian.drivers.processor;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.drivers.GatherMediaStatus;
import dev.yua.lynxiberian.drivers.ProcessorDriver;
import dev.yua.lynxiberian.models.FlickrMedia;
import dev.yua.lynxiberian.models.ProcessorResult;
import dev.yua.lynxiberian.repositories.FlickrMediaRepository;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FlickrProcessor extends ProcessorDriver {

    private File folderStorage;

    @Autowired
    public FlickrMediaRepository repository;

    @Override
    public String getName() { return "flickr"; }

    @Override
    public void onLoad() {
        this.folderStorage = new File("storage/flickr");
        this.folderStorage.mkdirs();
    }

    @Override
    public ProcessorResult process(JSONObject object) {
        ProcessorResult processorResult = new ProcessorResult();
        try {
            String id = object.getString("id");
            String source = object.getString("source");
            String mediaUrl = object.getString("media");
            String author = null;
            if(object.has("author")) {
                author = object.getString("author");
            }

            if(repository.flickrId(id) == null){
                File download = LynxiberianApplication.http.download(
                        mediaUrl,this.folderStorage.getPath()+"/"+FilenameUtils.getName(mediaUrl));

                FlickrMedia media = new FlickrMedia();
                media.setPath(download.getPath());
                media.setSource(source);
                media.setFlickrId(id);
                media.setAuthor(author);
                return processorResult.setMediaStatus(GatherMediaStatus.OK).addMedia(media);
            }else{
                FlickrMedia media = repository.flickrId(id);
                media.setSource(source);
                media.setFlickrId(id);
                media.setAuthor(author);
                return processorResult.setMediaStatus(GatherMediaStatus.OK).addMedia(media);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
