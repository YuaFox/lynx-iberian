package dev.yua.lynxiberian.drivers;

public abstract class Driver {

    private boolean ready = false;
    public abstract String getName();
    public abstract void onLoad();

    public void setReady(boolean ready){
        this.ready = ready;
    }

    public boolean isReady(){
        return ready;
    }
}
