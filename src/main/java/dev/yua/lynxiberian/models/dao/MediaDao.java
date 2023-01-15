package dev.yua.lynxiberian.models.dao;

import dev.yua.lynxiberian.repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.yua.lynxiberian.models.entity.Media;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Service
public class MediaDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MediaRepository mediaRepository;

    @Transactional(readOnly = true)
    public Media getRandom() {
        return (Media) em.createQuery("from Media ORDER BY RAND() LIMIT 1").getSingleResult();
    }

    @Transactional
    public void save(Media media){
        this.mediaRepository.save(media);
    }
    
}