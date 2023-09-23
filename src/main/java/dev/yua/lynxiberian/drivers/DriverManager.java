package dev.yua.lynxiberian.drivers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverManager {

    private List<Driver> drivers;
    private Map<String, GathererDriver> gathererDrivers;
    private Map<String, ExplorerDriver> explorerDrivers;
    private Map<String, PublishDriver> publishDrivers;

    public DriverManager(){
        this.drivers = new LinkedList<>();
        this.gathererDrivers = new HashMap<>();
        this.explorerDrivers = new HashMap<>();
        this.publishDrivers = new HashMap<>();
    }

    @Autowired
    public void setDrivers(List<Driver> drivers){
        this.drivers = drivers;
        this.drivers.forEach(this::addDriverByType);
    }

    private void addDriverByType(Driver driver){
        System.out.println("Adding driver: "+driver.getName());
        if(driver instanceof GathererDriver)
            this.gathererDrivers.put(driver.getName(), (GathererDriver) driver);
        else if(driver instanceof ExplorerDriver)
            this.explorerDrivers.put(driver.getName(), (ExplorerDriver) driver);
        else if(driver instanceof PublishDriver)
            this.publishDrivers.put(driver.getName(), (PublishDriver) driver);
    }

    public void triggerInit() {
        this.drivers.forEach(Driver::onLoad);
    }
    public GathererDriver getGathererDriver(String id){
        return this.gathererDrivers.get(id);
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