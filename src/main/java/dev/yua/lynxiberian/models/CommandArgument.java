package dev.yua.lynxiberian.models;

import java.util.List;
import java.util.Map;

public class CommandArgument {
    private String name;
    private String description;
    private boolean optional;
    private CommandArgumentType type;
    private Map<String, String> options;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public CommandArgumentType getType() {
        return type;
    }

    public void setType(CommandArgumentType type) {
        this.type = type;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
