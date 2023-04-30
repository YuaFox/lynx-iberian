package dev.yua.lynxiberian.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="discord_servers")
public class DiscordServer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private long guild;
    private long channel;
    private String token;

    private ScheduleTime scheduleTime;

    public long getGuild() {
        return guild;
    }

    public void setGuild(long guild) {
        this.guild = guild;
    }

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ScheduleTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(ScheduleTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public void edit(DiscordServer other){
        this.channel = other.channel;
        this.token = other.token;
        this.scheduleTime = other.scheduleTime;
    }
}
