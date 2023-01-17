package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.entity.Bucket;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.drivers.Driver;
import dev.yua.lynxiberian.models.dao.MediaDao;
import dev.yua.lynxiberian.models.entity.Media;
import org.springframework.stereotype.Component;

@Component
public abstract class GathererDriver extends Driver {

    @Autowired
    private MediaDao mediaDao;

    public abstract void gather();

    public int save(String driverName, JSONObject object){
        Media media = LynxiberianApplication.getDriverManager().getProcessorDriver(driverName).process(object);
        mediaDao.save(media);
        return 0;
    }
}