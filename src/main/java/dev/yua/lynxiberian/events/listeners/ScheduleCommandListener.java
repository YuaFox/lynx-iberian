package dev.yua.lynxiberian.events.listeners;

import dev.yua.lynxiberian.events.definitions.CommandEvent;
import dev.yua.lynxiberian.models.*;
import dev.yua.lynxiberian.repositories.DiscordServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ScheduleCommandListener extends CommandListener {

    @Autowired
    private DiscordServerRepository discordServerRepository;

    @Override
    public Command getCommand() {
        Command command = new Command();
        command.setName("schedule");
        command.setDescription("Send pics/videos at a scheduled interval.");
        List<CommandArgument> arguments = new ArrayList<>();
        CommandArgument argument = new CommandArgument();
        argument.setName("every");
        argument.setDescription("The interval");
        argument.setType(CommandArgumentType.STRING);
        argument.setOptional(false);
        Map<String, String> options = new LinkedHashMap<>();
        for(ScheduleTime value : ScheduleTime.values()){
            options.put(value.getName(), value.toString());
        }
        argument.setOptions(options);
        arguments.add(argument);

        command.setArguments(arguments);
        return command;
    }

    @Override
    public Post onCommand(CommandEvent event) {
        Post post = new Post();
        try {
            long serverId = Long.parseLong((String) event.getOptions().get("guild"));
            long channelId = Long.parseLong((String) event.getOptions().get("channel"));
            ScheduleTime scheduleTime = ScheduleTime.valueOf((String) event.getOptions().get("every"));

            DiscordServer server = discordServerRepository.findById(serverId).orElse(null);
            if (server == null) {
                server = new DiscordServer();
                server.setGuild(serverId);
            }
            server.setChannel(channelId);
            server.setScheduleTime(scheduleTime);

            this.discordServerRepository.save(server);

            if(scheduleTime == ScheduleTime.MINUTE_30){
                post.setCaption("❌  This option is patreon only!");
                post.setDescription("Minimum every 1 hour.\nSupport the bot and the developer with a really low priced subscription <3");
                post.setUrl("https://www.patreon.com/yuafox");
                return post;
            }

            if(scheduleTime == ScheduleTime.NEVER){
                post.setCaption("✅  Schedule deleted");
            }else{
                post.setCaption("✅  Schedule updated to every "+scheduleTime.getName());
            }

        }catch (Exception e){
            post.setCaption("❌  Error");
            e.printStackTrace();
        }
        return post;
    }
}
