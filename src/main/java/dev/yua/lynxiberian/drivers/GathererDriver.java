package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.entity.Filter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.models.dao.MediaDao;
import dev.yua.lynxiberian.models.entity.Media;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class GathererDriver extends Driver {

    @Autowired
    private MediaDao mediaDao;

    public abstract void gather(List<Filter> filters, GatherResults results);

    public void save(String driverName, JSONObject object, List<Filter> filters, GatherResults results){
        List<Media> mediaList = LynxiberianApplication.getDriverManager().getProcessorDriver(driverName).process(object);
        if(mediaList == null) return;
        for(Media media : mediaList) {
            boolean passed = true;
            if (filters != null) {
                for (Filter filter : filters) {
                    passed &= filter.isOk(media);
                }
            }
            if (passed) {
                mediaDao.save(media);
                results.addMediaAdded();
            }else{
                results.addMediaFiltered();
            }
        }
    }
}