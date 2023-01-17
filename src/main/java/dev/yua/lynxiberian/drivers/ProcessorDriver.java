package dev.yua.lynxiberian.drivers;

import org.json.JSONObject;

import dev.yua.lynxiberian.drivers.Driver;
import dev.yua.lynxiberian.models.entity.Media;

public abstract class ProcessorDriver extends Driver {

    public abstract Media process(JSONObject object);

}