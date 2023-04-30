package dev.yua.lynxiberian.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommandInputManager {

    private final Logger logger = LogManager.getLogger(CommandInputManager.class);

    private final Map<String, CommandInput> commandInputs;

    public CommandInputManager(){
        this.commandInputs = new HashMap<>();
    }

    @Autowired
    public void setCommandInputs(List<CommandInput> commandInputs){
        for(CommandInput commandInput : commandInputs){
            this.commandInputs.put(commandInput.getName(), commandInput);
        }
    }

    public void onLoad(){
        logger.info("Loading command inputs:");
        this.commandInputs.forEach((String key, CommandInput commandInput) -> {
            commandInput.onLoad();
            logger.info("{}",key);
        });
    }
}
