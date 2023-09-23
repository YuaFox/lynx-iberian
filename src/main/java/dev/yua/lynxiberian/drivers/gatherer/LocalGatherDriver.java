package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.repositories.BucketRepository;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class LocalGatherDriver extends GathererDriver {

    @Getter(lazy = true)
    private final String name = "local";

    @Autowired
    private BucketRepository bucketRepository;

    private final File folder;
    private final File folderStorage;

    public LocalGatherDriver(){
        this.folder = new File("config/local");
        this.folderStorage = new File("storage/local");
    }

    @Override
    public void onLoad() {
        this.folder.mkdirs();
        this.folderStorage.mkdirs();
        this.bucketRepository.findAll().forEach(
            (Bucket bucket) -> new File(this.folder, bucket.getName()).mkdir()
        );
        this.gather(null);
    }

    @Override
    public GatherResult gather(GatherRequest gatherRequest) {
        return this.gather(gatherRequest, this.folder);
    }

    // TODO: Sum of GatherResult
    private GatherResult gather(GatherRequest gatherRequest, @NonNull File directory) {
        GatherResult gatherResult = new GatherResult();
        File[] files = directory.listFiles();
        if(files == null) return gatherResult;

        for(File file : files){
            if(file.isDirectory()) {
                this.gather(new GatherRequest(bucketRepository.getBucketByName(file.getName()), null), file);
            }else{
                Path source = Paths.get(file.getPath());
                Path destination = Paths.get("storage/local/"+source.getFileName());
                try {
                    Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Media media = new Media();
                media.setPath(destination.toString());
                this.save(gatherRequest, media);
            }
        }
        return gatherResult;
    }
}