package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Config;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface ConfigDao extends CrudRepository<Config, Integer> {
    /**
     * @param key
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    @Deprecated
    @Query(value = "SELECT * FROM config WHERE key = :key", nativeQuery = true)
    Optional<Config> findConfigByKey(@Param("key") String key);
}
