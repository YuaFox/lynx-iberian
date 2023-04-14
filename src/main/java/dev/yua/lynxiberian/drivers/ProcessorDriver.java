package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.ProcessorResult;
import org.json.JSONObject;

public abstract class ProcessorDriver extends Driver {

    public abstract ProcessorResult process(JSONObject object);

}