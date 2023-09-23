package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.repositories.MediaRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class GathererDriver extends Driver {

    @Autowired
    private MediaRepository mediaRepository;

    public abstract GatherResult gather(GatherRequest gatherRequest);

    public void save(@NotNull GatherRequest request, @NotNull Media media){
        media.setBucket(request.getBucket());
        mediaRepository.save(media);
    }
}