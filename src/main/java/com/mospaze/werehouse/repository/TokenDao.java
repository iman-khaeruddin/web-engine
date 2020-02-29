package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Token;
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
public interface TokenDao extends CrudRepository<Token, Integer> {
    @Modifying
    @Query(value = "UPDATE token t SET flag_active = :flag_active WHERE token = :token AND username = :username AND flag_active = 1", nativeQuery = true)
    void updateToken(@Param("flag_active") int flag_active, @Param("token") String session, @Param("username") String username);
}
