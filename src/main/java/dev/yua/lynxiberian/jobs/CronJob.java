package dev.yua.lynxiberian.jobs;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
public class CronJob {
    /*
     ┌───────────── second (0-59)
     │ ┌───────────── minute (0 - 59)
     │ │ ┌───────────── hour (0 - 23)
     │ │ │ ┌───────────── day of the month (1 - 31)
     │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
     │ │ │ │ │ ┌───────────── day of the week (0 - 7)
     │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)
     │ │ │ │ │ │
     * * * * * *
     */
    @Scheduled(cron = "0 * * * * *")
    public void everyMinute() {
        this.send("minutely");
    }
    @Scheduled(cron = "0 0 * * * *")
    public void everyHour() {
        this.send("hourly");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void everyDay() {
        this.send("daily");
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void everyMonth() {
        this.send("monthly");
    }

    public void send(String event) {
        new Thread(() -> {
            try {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://"+System.getenv("NODERED_HOST")+":1880/lynx/api/v1/event/"+System.getenv("LYNX_USERNAME")+"/"+event);

                String json = "{\"password\":\""+System.getenv("LYNX_PASSWORD")+"\"}";
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                CloseableHttpResponse response = client.execute(httpPost);
                if(response.getCode() != 200)
                    System.err.println("Event not send: "+response);
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}