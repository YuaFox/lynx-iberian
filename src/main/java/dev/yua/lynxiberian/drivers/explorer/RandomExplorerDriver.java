package dev.yua.lynxiberian.drivers.explorer;

import dev.yua.lynxiberian.drivers.ExplorerDriver;
import dev.yua.lynxiberian.repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.yua.lynxiberian.models.Media;

@Component
public class RandomExplorerDriver extends ExplorerDriver {

    @Autowired
    private MediaRepository mediaRepository;

    @Override
    public String getName() { return "random"; }

    @Override
    public void onLoad() {
    }

    @Override
    public Media getMedia() {
        return mediaRepository.getRandomMedia();
    }
}
