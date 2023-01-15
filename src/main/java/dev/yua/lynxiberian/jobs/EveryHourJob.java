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
public class EveryHourJob {
    @Scheduled(cron = "0 0 * * * *")
    public void run() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://"+System.getenv("NODERED_HOST")+":1880/lynx/api/v1/event/"+System.getenv("LYNX_USERNAME")+"/hourly");

        String json = "{\"password\":\""+System.getenv("LYNX_PASSWORD")+"\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        if(response.getCode() != 200)
            System.err.println("Event not send: "+response);
        client.close();
    }
}