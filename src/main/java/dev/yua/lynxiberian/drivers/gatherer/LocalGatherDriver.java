package dev.yua.lynxiberian.drivers.gatherer;

import dev.yua.lynxiberian.drivers.GatherResult;
import dev.yua.lynxiberian.drivers.GathererDriver;
import dev.yua.lynxiberian.models.entity.Filter;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class LocalGatherDriver extends GathererDriver {
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

        this.gather(null, new GatherResult());
    }

    @Override
    public void gather(List<Filter> filters, GatherResult results) {
        for(File file : this.folder.listFiles()){
            JSONObject object = new JSONObject();
            object.put("path", file.getPath());
            this.save("local", object, filters, results);
        }
    }
}