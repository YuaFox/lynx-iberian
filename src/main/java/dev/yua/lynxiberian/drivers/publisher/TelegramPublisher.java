package dev.yua.lynxiberian.drivers.publisher;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;
import dev.yua.lynxiberian.drivers.PublishDriver;
import dev.yua.lynxiberian.models.Post;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class TelegramPublisher extends PublishDriver {

    private TelegramBot bot;
    private long chatId;

    @Override
    public String getName() {
        return "telegram";
    }

    @Override
    public void onLoad() {
        if(System.getenv("TELEGRAM_TOKEN") == null) return;

        try {
            this.bot = new TelegramBot(System.getenv("TELEGRAM_TOKEN"));
            this.chatId = Long.parseLong(System.getenv("TELEGRAM_CHAT"));
            this.setReady(true);
        }catch (Exception exception){
            System.err.println("Error setting up telegram!");
        }
    }



    @Override
    public void publish(Post post) {
        SendPhoto sendPhoto = new SendPhoto(this.chatId, new File(post.getPath()));
        if(post.getCaption() != null) {
            sendPhoto.caption(post.getCaption());
        }
        bot.execute(sendPhoto);
    }
}
