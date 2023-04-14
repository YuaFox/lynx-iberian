package dev.yua.lynxiberian.drivers.processor;

import dev.yua.lynxiberian.drivers.GatherMediaStatus;
import dev.yua.lynxiberian.drivers.ProcessorDriver;
import dev.yua.lynxiberian.models.ProcessorResult;
import dev.yua.lynxiberian.models.Media;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class LocalProcessorDriver extends ProcessorDriver {

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
    }

    @Override
    public ProcessorResult process(JSONObject object) {
        ProcessorResult processorResult = new ProcessorResult();
        Path source = Paths.get(object.getString("path"));
        Path fileName = source.getFileName();
        Path destination = Paths.get("storage/local/"+fileName);
        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Media media = new Media();
        media.setPath(destination.toString());
        return processorResult.setMediaStatus(GatherMediaStatus.OK).addMedia(media);
    }
}
