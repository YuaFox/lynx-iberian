package dev.yua.lynxiberian.models;

import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum ScheduleTime {

    NEVER("never", null),
    //MINUTE_1("minute", CronExpression.parse("0 * * * * *")),
    MINUTE_30("30 minutes", CronExpression.parse("0 */30 * * * *")),
    HOUR_1("hour",      CronExpression.parse("0 0 * * * *")),
    HOUR_2("2 hours",   CronExpression.parse("0 0 */2 * * *")),
    HOUR_4("4 hours",   CronExpression.parse("0 0 */4 * * *")),
    HOUR_12("12 hours", CronExpression.parse("0 0 */12 * * *")),
    DAY_1("day",        CronExpression.parse("0 0 0 * * *"));

    private final String name;
    private final CronExpression value;

    ScheduleTime(String key, CronExpression value){
        this.name = key;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public CronExpression getValue() {
        return value;
    }

    public boolean isNow(){
        if(this.value == null) return false;
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime nextRun = this.value.next(currentTime.minusNanos(1));
        return currentTime.equals(nextRun);
    }
}
