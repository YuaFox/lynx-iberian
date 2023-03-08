package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.entity.Filter;
import org.json.JSONObject;

import dev.yua.lynxiberian.drivers.Driver;
import dev.yua.lynxiberian.models.entity.Media;

import java.util.List;

public abstract class ProcessorDriver extends Driver {

    public abstract List<Media> process(JSONObject object);

}