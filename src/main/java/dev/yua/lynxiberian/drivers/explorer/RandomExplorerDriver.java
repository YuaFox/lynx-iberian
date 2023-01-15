package dev.yua.lynxiberian.drivers.explorer;

import dev.yua.lynxiberian.drivers.ExplorerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.yua.lynxiberian.models.dao.MediaDao;
import dev.yua.lynxiberian.models.entity.Media;

@Component
public class RandomExplorerDriver extends ExplorerDriver {

    @Override
    public String getName() { return "random"; }
    
    @Autowired
    private MediaDao mediaDao;

    @Override
    public void onLoad() {
    }

    @Override
    public Media getMedia() {
        return mediaDao.getRandom();
    }
}
