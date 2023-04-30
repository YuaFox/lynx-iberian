package dev.yua.lynxiberian.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.yua.lynxiberian.models.Command;
import dev.yua.lynxiberian.events.listeners.CommandListener;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class EventManager {
    private List<EventListener<?, ?>> eventListeners;

    public EventManager(){
        this.eventListeners = new LinkedList<>();
    }

    @Autowired
    public void setEventListeners(List<EventListener<?, ?>> eventListeners){
        this.eventListeners = eventListeners;
    }

    public List<Command> getCommands(){
        List<Command> commands = new ArrayList<>();
        for(EventListener<?, ?> listener : this.eventListeners){
            if(listener instanceof CommandListener){
                commands.add(((CommandListener) listener).getCommand());
            }
        }
        return commands;
    }

    public <T> T sendEvent(Event event, Class<T> c){
        T response = this.sendRemoteEvent(event, c);
        if(response != null) return response;

        for(EventListener<?, ?> listener : this.eventListeners){
            try {
                EventListener<Event, T> listener1 = (EventListener<Event, T>) listener;
                T o = listener1.onEvent(event);
                if (o != null) return o;
            }catch (ClassCastException ignored){

            }
        }
        return null;
    }

    private <T> T sendRemoteEvent(Event event, Class<T> c){
        if(System.getenv("WEBHOOK_EVENT") == null) return null;

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(System.getenv("WEBHOOK_EVENT"));

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject body = new JSONObject(ow.writeValueAsString(event));

            StringEntity entity = new StringEntity(body.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            if(response.getCode() != 200)
                return null;

            ObjectMapper objectMapper = new ObjectMapper();
            T result = objectMapper.readValue(response.getEntity().getContent(), c);
            client.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
