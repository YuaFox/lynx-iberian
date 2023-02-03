package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.entity.Post;

public abstract class PublishDriver extends Driver {
    public abstract void publish(Post post);
}