package dev.yua.lynxiberian.drivers.publisher;

import dev.yua.lynxiberian.bots.DiscordBot;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.DiscordServer;
import dev.yua.lynxiberian.models.Post;
import dev.yua.lynxiberian.repositories.DiscordServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscordPublisher extends PublishDriver {

    @Autowired
    private DiscordServerRepository repository;

    @Override
    public void onLoad() {
    }

    @Override
    public String getName() {
        return "discord";
    }

    @Override
    public boolean isReady() {
        return DiscordBot.getBot().enabled();
    }

    @Override
    public void publish(Post post) {
        for(DiscordServer server : this.repository.findAll()){
            try {
                DiscordBot.getBot().post(server, post);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
