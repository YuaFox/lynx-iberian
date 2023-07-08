package dev.yua.lynxiberian.repositories;

import dev.yua.lynxiberian.models.Bucket;
import dev.yua.lynxiberian.models.Media;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MediaRepository extends CrudRepository<Media, Long>, PagingAndSortingRepository<Media, Long> {
    @Query("from Media ORDER BY RAND() LIMIT 1")
    Media getRandomMedia();

    @Query("select m from Media m JOIN m.bucket where m.bucket = :bucket ORDER BY RAND() LIMIT 1")
    Media getRandomMedia(Bucket bucket);
}
