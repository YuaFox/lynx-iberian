package dev.yua.lynxiberian.repositories;

import dev.yua.lynxiberian.models.entity.RedditMedia;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedditMediaRepository  extends CrudRepository<RedditMedia, Long> {
    List<RedditMedia> permalink(String permalink);
}
