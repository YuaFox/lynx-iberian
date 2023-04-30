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
        if(post.getUrl() != null) {
            embedBuilder.addField(new MessageEmbed.Field("Source", "", true));
            embedBuilder.addField(new MessageEmbed.Field(post.getUrl(), "", true));
        }
        return embedBuilder.build();
    }

    public void post(DiscordServer server, Post post){
        System.out.println(server.getGuild());
        if(!server.getScheduleTime().isNow()){
            System.out.println("Not now next schedule at "+server.getScheduleTime().getValue().next(LocalDateTime.now().minusNanos(1)));
            System.out.println("Current time "+LocalDateTime.now());
            return;
        }

        Guild guild = this.bot.getGuildById(server.getGuild());
        if(guild == null) return;
        System.out.println(server.getChannel());
        TextChannel channel = guild.getTextChannelById(server.getChannel());
        if(channel == null){
            System.out.println("No channel defined");
            return;
        }
        MessageCreateAction message = channel.sendMessageEmbeds(this.getEmbed(post));

        if(post.getPath() != null) {
            File file = new File(post.getPath());
            message.addFiles(FileUpload.fromData(file, file.getName()));
        }
        message.queue();
        System.out.println("Sent");
    }
}
