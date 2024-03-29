package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.repositories.MediaRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import dev.yua.lynxiberian.LynxiberianApplication;
import org.springframework.stereotype.Component;

@Component
public abstract class GathererDriver extends Driver {

    @Autowired
    private MediaRepository mediaRepository;

    public abstract GatherResult gather(GatherRequest gatherRequest);

    public GatherMediaStatus save(String driverName, JSONObject object, GatherRequest request){
        ProcessorResult gatherResult = LynxiberianApplication.getDriverManager().getProcessorDriver(driverName).process(object);

        if(gatherResult.getMediaStatus() == GatherMediaStatus.DUPLICATED) return GatherMediaStatus.DUPLICATED;
        if(gatherResult.getMediaStatus() == GatherMediaStatus.ERROR) return GatherMediaStatus.ERROR;
        if(gatherResult.isEmpty()) return GatherMediaStatus.EMPTY;

        for(Media media : gatherResult.getMediaList()) {
            if(request == null || request.getFilter() == null || request.getFilter().isOk(media)){

                if (request != null && request.getBucket() != null) {
                    media.setBucket(request.getBucket());
                }
                mediaRepository.save(media);
                gatherResult.setMediaStatus(GatherMediaStatus.OK);
            }else{
                return GatherMediaStatus.FILTERED;
            }
        }

        return gatherResult.getMediaStatus();
    }
}