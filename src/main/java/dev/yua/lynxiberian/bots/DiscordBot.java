package dev.yua.lynxiberian.bots;


import dev.yua.lynxiberian.models.DiscordServer;
import dev.yua.lynxiberian.models.Post;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DiscordBot {

    private static final DiscordBot instance = new DiscordBot();

    public static DiscordBot getBot(){
        return DiscordBot.instance;
    }
    private final JDA bot;

    private DiscordBot() {
        if(System.getenv("DISCORD_TOKEN") == null || System.getenv("DISCORD_TOKEN").isEmpty()){
            this.bot = null;
            return;
        }
        try {
            this.bot = JDABuilder.createLight(System.getenv("DISCORD_TOKEN"), Collections.emptyList())
                    .setActivity(Activity.playing(System.getenv("DISCORD_ACTIVITY")))
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean enabled(){
        return this.bot != null;
    }

    public void addEventListener(Object o){
        if(this.bot == null) return;
        this.bot.addEventListener(o);
    }

    public JDA getJda(){
        return this.bot;
    }


    public MessageEmbed getEmbed(Post post){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(post.getCaption() != null && !post.getCaption().isBlank())
            embedBuilder.setTitle(post.getCaption());
        if(post.getPath() != null)
            embedBuilder.setImage("attachment://"+new File(post.getPath()).getName());
        if(post.getDescription() != null)
            embedBuilder.setDescription(post.getDescription());
        if(post.getAuthor() != null){
            embedBuilder.addField(new MessageEmbed.Field("Author", post.getAuthor(), false));
        }else{
            embedBuilder.addField(new MessageEmbed.Field("Author", "Unknown", false));
        }
        if(post.getUrl() != null) {
            embedBuilder.addField(new MessageEmbed.Field("Source", post.getUrl(), false));
        }
        return embedBuilder.build();
    }

    public void post(DiscordServer server, Post post){
        try {
            if (!server.getScheduleTime().isNow()) {
                return;
            }

            Guild guild = this.bot.getGuildById(server.getGuild());
            if (guild == null) return;
            StandardGuildMessageChannel channel = guild.getTextChannelById(server.getChannel());
            if (channel == null) channel = guild.getNewsChannelById(server.getChannel());
            guild.getNewsChannelById(server.getChannel());
            if (channel == null) {
                return;
            }

            MessageCreateAction message = channel.sendMessageEmbeds(this.getEmbed(post));

            if (post.getPath() != null) {
                File file = new File(post.getPath());
                message.addFiles(FileUpload.fromData(file, file.getName()));
            }
            message.queue();
        }catch (InsufficientPermissionException e){

        }
    }
}
