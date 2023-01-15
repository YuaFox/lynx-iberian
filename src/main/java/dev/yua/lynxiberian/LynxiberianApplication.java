package dev.yua.lynxiberian;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import dev.yua.lynxiberian.utils.BeanInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.yua.lynxiberian.drivers.DriverManager;
import dev.yua.lynxiberian.drivers.explorer.RandomExplorerDriver;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LynxiberianApplication {

	public static LynxiberianApplication instance;

	@Autowired
	private DriverManager driverManager;
	@Autowired
	private BeanInjector beanInjector;

	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
		SpringApplication.run(LynxiberianApplication.class, args);
	}

	public static DriverManager getDriverManager(){
		return instance.driverManager;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onLoad() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		instance = this;
		beanInjector.loadBeans(new File("plugins"));
		driverManager.loadDrivers(new File("plugins"));
		driverManager.addDriver(RandomExplorerDriver.class);
		driverManager.triggerInit();
	}
}
