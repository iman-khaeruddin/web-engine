package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.TransactionList;
import com.mospaze.werehouse.entity.TransactionListNew;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 04/12/2019
 */
public interface CustomTransactionListDao {
    /**
     * @param warehouse
     * @param status
     * @param page
     * @param size
     * @return list of {@link TransactionList}
     */
    @Deprecated
    List<TransactionList> findJobList(String warehouse, String status, Integer page, Integer size);

    /**
     * @param warehouse
     * @param status
     * @param page
     * @param size
     * @return {@link BigDecimal}
     */
    @Deprecated
    BigDecimal getTotalPage(String warehouse, String status, Integer page, Integer size);

    /**
     * @param warehouse
     * @param status
     * @param page
     * @param size
     * @return {@link BigInteger}
     */
    @Deprecated
    BigInteger getTotalElement(String warehouse, String status, Integer page, Integer size);

    /**
     * @param user_id
     * @param warehouse
     * @param status
     * @param page
     * @param size
     * @return {@link TransactionListNew}
     */
    List<TransactionListNew> findJobListNew(int user_id, int warehouse, String status, int page, int size);

    /**
     * @param user_id
     * @param warehouse
     * @param status
     * @param page
     * @param size
     * @return {@link BigDecimal}
     */
    BigDecimal getTotalPageNew(int user_id, int warehouse, String status, int page, int size);

    /**
     * @param user_id
     * @param warehouse
     * @param status
     * @param page
     * @param size
     * @return {@link BigInteger}
     */
    BigInteger getTotalElementNew(int user_id, int warehouse, String status, int page, int size);
}
