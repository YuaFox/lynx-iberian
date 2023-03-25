package dev.yua.lynxiberian.drivers;

import org.json.JSONObject;

public abstract class ProcessorDriver extends Driver {

    public abstract ProcessorResult process(JSONObject object);

}