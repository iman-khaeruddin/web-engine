package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Otp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface OtpDao extends PagingAndSortingRepository<Otp, Integer> {
    @Query(value = "SELECT * FROM otp WHERE phone = :phone", nativeQuery = true)
    List<Otp> findByPhone(@Param("phone") String phone);

    @Query(value = "SELECT * FROM otp WHERE phone = :phone AND otp = :otp", nativeQuery = true)
    List<Otp> findByPhoneAndOtp(@Param("phone") String phone, @Param("otp") String otp);

    @Query(value = "SELECT * FROM otp ", nativeQuery = true)
    Page<Otp> findAllPageable(Pageable pageable);
}
