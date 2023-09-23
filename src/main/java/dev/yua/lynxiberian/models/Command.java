package dev.yua.lynxiberian.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
public class Command {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private List<CommandArgument> arguments;
    @Getter @Setter
    private boolean adminRequired;
}
