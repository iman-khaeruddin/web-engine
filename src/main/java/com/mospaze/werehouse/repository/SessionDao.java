package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface SessionDao extends CrudRepository<Session, Integer> {
    @Modifying
    @Query(value = "UPDATE session s SET timestamp = :timestamp WHERE session = :session AND username = :username AND flag_active = 1", nativeQuery = true)
    int updateSession(@Param("timestamp")Timestamp timestamp, @Param("session") String session, @Param("username") String username);

    @Modifying
    @Query(value = "UPDATE session s SET flag_active = 0 WHERE session = :session AND username = :username", nativeQuery = true)
    void killSession(@Param("session") String session, @Param("username") String username);
}
