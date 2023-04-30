package dev.yua.lynxiberian.jobs;

import dev.yua.lynxiberian.LynxiberianApplication;
import dev.yua.lynxiberian.events.EventResult;
import dev.yua.lynxiberian.events.definitions.TimeEvent;
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

    @Scheduled(cron = "0 0 * * * *")
    public void everyHour() {
        LynxiberianApplication.getEventManager().sendEvent(new TimeEvent("post"), EventResult.class);
    }
}
