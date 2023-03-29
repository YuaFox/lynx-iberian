package dev.yua.lynxiberian.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.yua.lynxiberian.drivers.DriverManager;
import dev.yua.lynxiberian.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GatherRequest {
    private Bucket bucket;
    private Filter filter;

    public Bucket getBucket() {
        return bucket;
    }

    public GatherRequest setBucket(Bucket bucket) {
        this.bucket = bucket;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
