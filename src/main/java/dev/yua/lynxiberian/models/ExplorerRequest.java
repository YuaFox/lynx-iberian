package dev.yua.lynxiberian.models;

import org.springframework.stereotype.Component;

@Component
public class ExplorerRequest {

    private Bucket bucket;

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }
}
