package com.rojunaid.roacademy.repositories;

import com.rojunaid.roacademy.models.TeachingResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TeachingResourceRepository extends CrudRepository<TeachingResource, Long> {

  Optional<TeachingResource> findByTitle(String name);

//  @Query("SELECT tr FROM TeachingResource tr JOIN tr.tags t WHERE t.name = :tagName")
//  Iterable<TeachingResource> findByTag(String tagName);

  @Query(
      value =
          "SELECT * FROM teaching_resource tr WHERE tr.resource_owner_id = ?1 AND tr.resource_owner_type = ?2",
      nativeQuery = true)
  Set<TeachingResource> findByResourceIdAndType(Long resourceableId, String resourceableType);
}
