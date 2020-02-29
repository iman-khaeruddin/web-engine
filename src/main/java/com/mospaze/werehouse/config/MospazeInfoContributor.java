package com.mospaze.werehouse.config;

import com.mospaze.werehouse.common.Constant;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@Component
public class MospazeInfoContributor implements InfoContributor {
 
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("startup_date", Constant.configProperties.get(Constant.STARTUP_DATE));
    }
}