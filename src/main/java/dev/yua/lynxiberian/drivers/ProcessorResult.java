package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.entity.Media;

import java.util.ArrayList;
import java.util.List;

public class ProcessorResult {
    private List<Media> mediaList;
    private GatherMediaStatus mediaStatus;

    public ProcessorResult() {
        this.mediaList = new ArrayList<>();
        this.mediaStatus = GatherMediaStatus.EMPTY;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public ProcessorResult addMedia(Media media){
        this.mediaList.add(media);
        return this;
    }

    public boolean isEmpty(){
        return this.mediaStatus == GatherMediaStatus.EMPTY || this.mediaList.isEmpty();
    }

    public GatherMediaStatus getMediaStatus() {
        return mediaStatus;
    }

    public ProcessorResult setMediaStatus(GatherMediaStatus mediaStatus) {
        this.mediaStatus = mediaStatus;
        return this;
    }
}
