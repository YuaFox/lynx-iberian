package dev.yua.lynxiberian.models;

import java.util.List;
public class Command {
    private String name;
    private String description;
    private List<CommandArgument> arguments;
    private boolean adminRequired;

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

    public List<CommandArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<CommandArgument> arguments) {
        this.arguments = arguments;
    }

    public boolean isAdminRequired() {
        return adminRequired;
    }

    public void setAdminRequired(boolean adminRequired) {
        this.adminRequired = adminRequired;
    }
}
