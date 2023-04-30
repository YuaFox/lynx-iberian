package dev.yua.lynxiberian.commands;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.bots.DiscordBot;
import dev.yua.lynxiberian.models.Command;
import dev.yua.lynxiberian.models.CommandArgument;
import dev.yua.lynxiberian.models.Post;
import dev.yua.lynxiberian.events.definitions.CommandEvent;
import dev.yua.lynxiberian.events.definitions.CommandListEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class DiscordCommandInput extends CommandInput {
    @Override
    public String getName() {
        return "discord";
    }

    @Override
    public void onLoad() {
        if(!DiscordBot.getBot().enabled()) return;
        DiscordBot bot = DiscordBot.getBot();
        bot.addEventListener(new DiscordEventListener());
        List<Command> localCommands = LynxiberianApplication.getEventManager().getCommands();
        Command[] remoteCommands = LynxiberianApplication.getEventManager().sendEvent(new CommandListEvent(), Command[].class);
        List<SlashCommandData> discordCommands = new LinkedList<>();

        List<Command> commands = new ArrayList<>(localCommands);
        if(remoteCommands != null)
            commands.addAll(List.of(remoteCommands));

        for(Command command : commands){
            SlashCommandData discordCommand = Commands.slash(command.getName(), command.getDescription());

            if(command.isAdminRequired()) {
                discordCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
            }

            if(command.getArguments() != null) {
                for (CommandArgument argument : command.getArguments()) {
                    OptionType option = switch (argument.getType()) {
                        case STRING -> OptionType.STRING;
                        case INTEGER -> OptionType.INTEGER;
                        case USER -> OptionType.USER;
                    };

                    OptionData optionData = new OptionData(option,
                            argument.getName(),
                            argument.getDescription(),
                            !argument.isOptional());

                    if(argument.getOptions() != null){
                        for(Map.Entry<String, String> kv: argument.getOptions().entrySet()){
                            optionData.addChoice(kv.getKey(), kv.getValue());
                        }
                    }

                    discordCommand.addOptions(optionData);
                }
            }

            discordCommands.add(discordCommand);
        }

        bot.getJda().updateCommands().addCommands(discordCommands).queue();
        this.setReady(true);
    }
}

class DiscordEventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        Map<String, Object> options = new HashMap<>();

        for(OptionMapping option : event.getOptions()){
            options.put(option.getName(), option.getAsString());
        }

        options.put("guild", event.getGuild().getId());
        options.put("channel", event.getChannel().getId());
        options.put("user", event.getUser().getId());

        Post post = LynxiberianApplication.getEventManager().sendEvent(new CommandEvent(event.getName(), options), Post.class);

        File file = null;
        if(post.getPath() != null)
            file = new File(post.getPath());

        ReplyCallbackAction reply = event.replyEmbeds(DiscordBot.getBot().getEmbed(post));
        if(file != null) {
            reply.addFiles(FileUpload.fromData(file, file.getName()));
        }
        reply.queue();
    }
}
