package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.entity.Media;
import com.mospaze.werehouse.repository.MediaDao;
import com.mospaze.werehouse.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MediaService
 */
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDao mediaDao;

    @Override
    public List<Media> getAllMediaWarehouse(int model_id) {
        return mediaDao.getAllMediaWarehouse(model_id);
    }

	@Override
	public List<Media> getMediaWarehouse(int model_id) {
		return mediaDao.getMediaWarehouse(model_id);
	}

    @Override
    public Media save(Media media) {
        return mediaDao.save(media);
    }

    @Override
    public void removeMainImage(int warehouse_id) {
        mediaDao.removeMainImage(warehouse_id);
    }

    @Override
    public void setMainImage( int image_id) {
        mediaDao.setMainImage(image_id);
    }
}
