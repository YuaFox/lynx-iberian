package dev.yua.lynxiberian.repositories;

import dev.yua.lynxiberian.models.FlickrMedia;
import dev.yua.lynxiberian.models.RedditMedia;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlickrMediaRepository extends CrudRepository<FlickrMedia, Long> {
    FlickrMedia flickrId(String flickrId);
}
