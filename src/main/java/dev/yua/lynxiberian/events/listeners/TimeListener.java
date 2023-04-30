package dev.yua.lynxiberian.events.listeners;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.events.EventListener;
import dev.yua.lynxiberian.events.EventResult;
import dev.yua.lynxiberian.events.definitions.TimeEvent;
import dev.yua.lynxiberian.models.ExplorerRequest;
import dev.yua.lynxiberian.models.Media;
import dev.yua.lynxiberian.models.Post;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeListener implements EventListener<TimeEvent, EventResult> {

    @Autowired
    BucketRepository bucketRepository;

    @Override
    public EventResult onEvent(TimeEvent event) {
        if(event.getTimeName().equals("post")){
            for(PublishDriver publishDriver : LynxiberianApplication.getDriverManager().getPublishDrivers()){
                if(publishDriver.isReady()){
                    ExplorerRequest explorerRequest = new ExplorerRequest();
                    explorerRequest.setBucket(bucketRepository.getBucketByName("default"));

                    Media random = LynxiberianApplication.getDriverManager().getExplorerDriver("random").getMedia(explorerRequest);

                    publishDriver.publish(new Post(random));
                }
            }
        }
        EventResult eventResult = new EventResult();
        eventResult.setSuccess(true);
        return eventResult;
    }
}
