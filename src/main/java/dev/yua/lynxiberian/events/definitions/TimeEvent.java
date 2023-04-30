package dev.yua.lynxiberian.events.definitions;

import dev.yua.lynxiberian.events.Event;

public class TimeEvent extends Event {

    private String timeName;

    public TimeEvent(){

    }
    public TimeEvent(String name){
        this.timeName = name;
    }

    @Override
    public String getName() {
        return "time";
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }
}
