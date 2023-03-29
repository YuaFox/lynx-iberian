package dev.yua.lynxiberian.repositories;

import org.springframework.data.repository.CrudRepository;

import dev.yua.lynxiberian.models.Bucket;

public interface BucketRepository extends CrudRepository<Bucket, Long> {
    Bucket getBucketByName(String name);
}
