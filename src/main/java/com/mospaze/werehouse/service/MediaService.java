package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.Media;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MediaService {
    /**
     * @param model_id
     * @return list of {@link Media}
     */
    List<Media> getAllMediaWarehouse(int model_id);

    /**
     * @param model_id
     * @return list of {@link Media}
     */
    List<Media> getMediaWarehouse(int model_id);

    /**
     * @param media
     * @return the saved entity will never be {@literal null}.
     */
    Media save(Media media);

    /**
     * @param warehouse_id
     */
    void removeMainImage(int warehouse_id);

    /**
     * @param image_id
     */
    void setMainImage(int image_id);
}
