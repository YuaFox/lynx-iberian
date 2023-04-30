package dev.yua.lynxiberian.events.definitions;

import dev.yua.lynxiberian.events.Event;

import java.util.Map;

public class CommandEvent extends Event {

    private String commandName;
    private Map<String, Object> options;

    public CommandEvent(String commandName, Map<String, Object> options){
        this.commandName = commandName;
        this.options = options;
    }

    @Override
    public String getName() {
        return "command";
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }
}
