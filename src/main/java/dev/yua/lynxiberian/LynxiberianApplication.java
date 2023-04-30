package dev.yua.lynxiberian;

import dev.yua.lynxiberian.commands.CommandInputManager;
import dev.yua.lynxiberian.events.EventManager;
import dev.yua.lynxiberian.utils.http.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.yua.lynxiberian.drivers.DriverManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LynxiberianApplication {

	public static LynxiberianApplication instance;
	public static Http http = new Http();

	@Autowired
	private DriverManager driverManager;
	@Autowired
	private EventManager eventManager;
	@Autowired
	private CommandInputManager commandInputManager;


	public static void main(String[] args) {
		SpringApplication.run(LynxiberianApplication.class, args);
	}

	public static DriverManager getDriverManager(){
		return instance.driverManager;
	}
	public static EventManager getEventManager(){
		return instance.eventManager;
	}
	public static CommandInputManager getCommandInputManager() { return instance.commandInputManager; }

	@EventListener(ApplicationReadyEvent.class)
	public void onLoad() {
		instance = this;
		driverManager.triggerInit();
		commandInputManager.onLoad();
	}
}