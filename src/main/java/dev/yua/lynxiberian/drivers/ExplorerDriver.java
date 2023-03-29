package dev.yua.lynxiberian.drivers;

import dev.yua.lynxiberian.models.ExplorerRequest;
import dev.yua.lynxiberian.models.Media;

public abstract class ExplorerDriver extends Driver {
    public abstract Media getMedia(ExplorerRequest explorerRequest);
}
