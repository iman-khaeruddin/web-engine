package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mospaze.werehouse.entity.Otp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpPageable implements Serializable {
    private List<Otp> detail_otp;
    private BigInteger total_page;
    private BigInteger total_element;

    @Override
    public String toString() {
        return "OtpPageable{" +
                "detail_otp=" + detail_otp +
                ", total_page=" + total_page +
                ", total_element=" + total_element +
                '}';
    }
}
