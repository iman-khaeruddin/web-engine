package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author Iman Khaeruddin
 * @since 07/12/2019
 */
public interface StockOnHandService {

    /**
     * @param file
     * @param buyer_id
     * @param jwt
     * @return {@link GeneralResponse}
     */
    GeneralResponse uploadStockOnHand(MultipartFile file, int buyer_id, JWT jwt) throws IOException, ParseException;
}
