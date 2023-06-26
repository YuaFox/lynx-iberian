package dev.yua.lynxiberian.events.listeners;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.events.definitions.CommandEvent;
import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.repositories.BucketRepository;
import dev.yua.lynxiberian.repositories.DiscordServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MainCommandListener extends CommandListener {

    @Autowired
    private BucketRepository bucketRepository;

    @Override
    public Command getCommand() {
        Command command = new Command();
        command.setName(Optional.ofNullable(System.getenv("MAIN_COMMAND_NAME")).orElse("boop"));
        command.setDescription(Optional.ofNullable(System.getenv("MAIN_COMMAND_DESCRIPTION")).orElse("boop!!!"));
        return command;
    }

    @Override
    public Post onCommand(CommandEvent event) {
        Post post = new Post();
        try {
            ExplorerRequest explorerRequest = new ExplorerRequest();
            explorerRequest.setBucket(bucketRepository.getBucketByName(Optional.ofNullable(System.getenv("MAIN_COMMAND_BUCKET")).orElse("default")));

            post = new Post(LynxiberianApplication.getDriverManager().getExplorerDriver("random").getMedia(explorerRequest));
        }catch (Exception e){
            post.setCaption("❌  Error");
            e.printStackTrace();
        }
        return post;
    }
}
