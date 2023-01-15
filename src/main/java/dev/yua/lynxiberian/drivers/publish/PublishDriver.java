package dev.yua.lynxiberian.drivers.publish;

import dev.yua.lynxiberian.drivers.Driver;
import dev.yua.lynxiberian.models.entity.Media;

public abstract class PublishDriver extends Driver {
    public abstract void publish(Media media);
}
