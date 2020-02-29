package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Media;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MediaDao extends CrudRepository<Media, Integer> {
    /**
     * @param id
     * @return list of {@link Media}
     */
    @Query(value = "SELECT  " +
             "    m.* " +
             "FROM " +
             "    media m " +
             "        JOIN " +
             "    warehouse w ON m.model_id = w.id " +
             "WHERE " +
             "    w.id = :id " +
             "        AND m.collection_name = 'warehouse'", nativeQuery = true)
    List<Media> getAllMediaWarehouse(@Param(value = "id") int id);

    /**
     * @param id
     * @return list of {@link Media} LIMIT 1
     */
    @Query(value = "SELECT  " +
            "    m.* " +
            "FROM " +
            "    media m " +
            "        JOIN " +
            "    warehouse w ON m.model_id = w.id " +
            "WHERE " +
            "    w.id = :id " +
            "        AND m.collection_name = 'warehouse' " +
            "ORDER BY order_column DESC " +
            "LIMIT 1", nativeQuery = true)
    List<Media> getMediaWarehouse(@Param(value = "id") int id);

    /**
     * @param model_id
     * @return list of {@link Media}
     */
    @Deprecated
    @Query(value = "SELECT  " +
            "    m.* " +
            "FROM " +
            "    media m " +
            "WHERE " +
            "    m.model_id = :model_id " +
            "        AND m.collection_name = 'warehouse'", nativeQuery = true)
    List<Media> findByModelId(@Param("model_id") int model_id);

    /**
     * @param id
     * @param limit
     * @return list of {@link Media}
     */
    @Query(value = "SELECT  " +
            "    m.* " +
            "FROM " +
            "    media m " +
            "        JOIN " +
            "    warehouse w ON m.model_id = w.id " +
            "WHERE " +
            "    w.id = :id " +
            "        AND m.collection_name = 'warehouse' LIMIT :limit", nativeQuery = true)
    List<Media> getAllMediaWarehouseUseLimit(@Param(value = "id") int id, @Param("limit") int limit);

    /**
     * @param warehouse_id
     */
    @Query(value = "UPDATE media SET order_column = 0 WHERE model_id = :warehouse_id", nativeQuery = true)
    @Modifying
    void removeMainImage(@Param(value = "warehouse_id") int warehouse_id);

    /**
     * @param image_id
     */
    @Query(value = "UPDATE media SET order_column = 1 WHERE id = :image_id", nativeQuery = true)
    @Modifying
    void setMainImage(@Param(value = "image_id") int image_id);
}
