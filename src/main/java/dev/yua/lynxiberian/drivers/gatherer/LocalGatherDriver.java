package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.models.GatherRequest;
import dev.yua.lynxiberian.models.GatherResult;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class LocalGatherDriver extends GathererDriver {

    @Autowired
    private BucketRepository bucketRepository;

    File folder;
    File folderStorage;

    @Override
    public String getName() { return "local"; }

    @Override
    public void onLoad() {
        this.folder = new File("config/local");
        this.folderStorage = new File("storage/local");
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
                this.gather(new GatherRequest().setBucket(bucketRepository.getBucketByName(file.getName())), file);
            }else{
                JSONObject object = new JSONObject();
                object.put("path", file.getPath());
                this.save("local", object, gatherRequest);
            }
        }
        return gatherResult;
    }
}