package dev.yua.lynxiberian.drivers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

import dev.yua.lynxiberian.drivers.gatherer.GathererDriver;
import dev.yua.lynxiberian.drivers.gatherer.ProcessorDriver;
import dev.yua.lynxiberian.drivers.publish.PublishDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DriverManager {

    @Autowired
    ConfigurableApplicationContext applicationContext;

    private List<Driver> drivers;
    private Map<String, GathererDriver> gathererDrivers;
    private Map<String, ProcessorDriver> processorDrivers;
    private Map<String, ExplorerDriver> explorerDrivers;
    private Map<String, PublishDriver> publishDrivers;

    public DriverManager(){
        this.drivers = new LinkedList<>();
        this.gathererDrivers = new HashMap<>();
        this.processorDrivers = new HashMap<>();
        this.explorerDrivers = new HashMap<>();
        this.publishDrivers = new HashMap<>();
    }

    public void loadDrivers(File folder) throws IOException {
        System.out.println(applicationContext);
        for(File file : folder.listFiles()) {
            System.out.println(file);
            JarFile jarFile = new JarFile(file);
            InputStream inputStream = jarFile.getInputStream(jarFile.getJarEntry(".properties"));
            Properties properties = new Properties();
            properties.load(inputStream);

            String[] classes = properties.getProperty("drivers").split(":");
            URL[] urls = { new URL("jar:file:" + file.getAbsolutePath()+"!/") };
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            
            

            for(String clazzName : classes){
                try {
                    Class<? extends Driver> c = this.getClass().getClassLoader().loadClass(clazzName).asSubclass(Driver.class);
                    //Class<? extends Driver> c = cl.loadClass(clazzName).asSubclass(Driver.class);
                    this.addDriver(c);
                }catch(Exception e){
                    System.out.println("Error");
                    e.printStackTrace(System.err);
                }
                jarFile.close();
            }
        }
    }

    public void addDriver(Class<? extends Driver> c) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Driver driver = this.applicationContext.getAutowireCapableBeanFactory().createBean(c);

        this.drivers.add(driver);

        if(driver instanceof GathererDriver){
            System.out.println("Loaded driver "+driver.getName()+":GathererDriver");
            this.gathererDrivers.put(driver.getName(), (GathererDriver) driver);
        }
        if(driver instanceof ProcessorDriver){
            System.out.println("Loaded driver "+driver.getName()+":ProcessorDriver");
            this.processorDrivers.put(driver.getName(), (ProcessorDriver) driver);
        }
        if(driver instanceof ExplorerDriver){
            System.out.println("Loaded driver "+driver.getName()+":ExplorerDriver");
            this.explorerDrivers.put(driver.getName(), (ExplorerDriver) driver);
        }
        if(driver instanceof PublishDriver){
            System.out.println("Loaded driver "+driver.getName()+":PublishDriver");
            this.publishDrivers.put(driver.getName(), (PublishDriver) driver);
        }
    }

    public void triggerInit() {
        this.drivers.forEach(Driver::onLoad);
    }

    public GathererDriver getGathererDriver(String id){
        return this.gathererDrivers.get(id);
    }
    public ProcessorDriver getProcessorDriver(String id){
        return this.processorDrivers.get(id);
    }

    public ExplorerDriver getExplorerDriver(String id){
        return this.explorerDrivers.get(id);
    }

    public PublishDriver getPublishDriver(String id){
        return this.publishDrivers.get(id);
    }
    public List<PublishDriver> getPublishDrivers(){
        return new LinkedList<>(this.publishDrivers.values());
    }
}