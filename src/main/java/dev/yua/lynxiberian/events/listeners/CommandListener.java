package dev.yua.lynxiberian.events.listeners;

import dev.yua.lynxiberian.events.Event;
import dev.yua.lynxiberian.events.EventListener;
import dev.yua.lynxiberian.events.EventResult;
import dev.yua.lynxiberian.events.definitions.CommandEvent;
import dev.yua.lynxiberian.models.Command;
import dev.yua.lynxiberian.models.Post;

public abstract class CommandListener implements EventListener<CommandEvent, Post> {


    public abstract Command getCommand();
    public abstract Post onCommand(CommandEvent event);

    @Override
    public Post onEvent(CommandEvent event) {
        if(this.getCommand().getName().equals(((CommandEvent) event).getCommandName())){
            return this.onCommand((CommandEvent) event);
        }
        return null;
    }
}
