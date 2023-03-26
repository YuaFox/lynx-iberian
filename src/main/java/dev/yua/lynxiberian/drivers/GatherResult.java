package dev.yua.lynxiberian.drivers;

public class GatherResult {

    // TODO: Add failed (duplicated, error)

    private int mediaTotal = 0;
    private int mediaAdded = 0;
    private int mediaFiltered = 0;


    public GatherResult(){

    }

    public void addMediaAdded(){
        this.mediaAdded++;
    }

    public void addMediaFiltered(){
        this.mediaFiltered++;
    }

    public int getMediaTotal() {
        return mediaTotal;
    }

    public void setMediaTotal(int mediaTotal) {
        this.mediaTotal = mediaTotal;
    }

    public int getMediaAdded() {
        return mediaAdded;
    }

    public void setMediaAdded(int mediaAdded) {
        this.mediaAdded = mediaAdded;
    }

    public int getMediaFiltered() {
        return mediaFiltered;
    }

    public void setMediaFiltered(int mediaFiltered) {
        this.mediaFiltered = mediaFiltered;
    }


}
