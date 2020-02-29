package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Users;
import com.mospaze.werehouse.entity.projector.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface UsersDao extends CrudRepository<Users, Integer> {

    @Query(value = "SELECT count(phone) as count FROM users where phone = :phone_number AND deleted_at IS NULL AND status = 1", nativeQuery = true)
    int countByPhoneNumber(@Param("phone_number") String phone_number);

    @Query(value = "SELECT   " +
            "    r.name AS user_type,  " +
            "    r.id AS role_id,  " +
            "    u.id,  " +
            "    u.email,  " +
            "    u.verified_phone,  " +
            "    u.status,  " +
            "    CONCAT_WS(' ', u.first_name, u.last_name) AS owner_name," +
            "    u.flag_change_password " +
            "FROM  " +
            "    roles r  " +
            "        JOIN  " +
            "    model_has_roles mrh ON mrh.role_id = r.id  " +
            "        JOIN  " +
            "    users u ON mrh.model_id = u.id  " +
            "WHERE  " +
            "    u.phone = :username  " +
            "        AND u.password = :password  " +
            "        AND u.deleted_at IS NULL  " +
            "        AND u.status = 1", nativeQuery = true)
    Optional<User> verify(@Param("username") String username, @Param("password") String password);

    @Query(value = "SELECT count(id) as count FROM users where phone = :username AND deleted_at IS NULL AND status = 1", nativeQuery = true)
    int isUsernameRegistered(@Param("username") String username);

    @Query(value = "SELECT count(id) as count FROM users where email = :email AND deleted_at IS NULL AND status = 1", nativeQuery = true)
    int isEmailRegistered(@Param("email") String email);

    @Query(value = "SELECT * FROM users where email = :email AND deleted_at IS NULL AND status = 1", nativeQuery = true)
    Users getUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users where phone = :phone_number AND deleted_at IS NULL AND status = 1", nativeQuery = true)
    Optional<Users> getUserByPhone(@Param("phone_number") String phone_number);

    @Modifying
    @Query(value = "UPDATE users w SET deleted_at = now(), email = CONCAT(email,'-',:id), phone = CONCAT(phone,'-',:id), username = CONCAT(username,'-',:id) WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") int id);

    @Modifying
    @Query(value = "UPDATE users w SET status = :status WHERE id = :id", nativeQuery = true)
    void changeStatusUser(@Param("status") int status, @Param("id") int id);

    @Modifying
    @Query(value = "update users set first_name = :first_name, " +
            "last_name = :last_name, " +
            "company = :company, " +
            "address_1 = :address, " +
            "postalcode = :postalcode, " +
            "city_id = :city_id, " +
            "state_id = :state_id, " +
            "country_id = :country_id, " +
            "updated_at = now() " +
            "where " +
            "id = :id", nativeQuery = true)
    @Deprecated
    void updateUser(@Param("first_name") String first_name,
                    @Param("last_name") String last_name,
                    @Param("company") String company,
                    @Param("address") String address,
                    @Param("postalcode") String postalcode,
                    @Param("city_id") Integer city_id,
                    @Param("state_id") Integer state_id,
                    @Param("country_id") Integer country_id,
                    @Param("id") int id);

    @Query(value = "SELECT   " +
            "    r.name AS user_type,  " +
            "    r.id AS role_id,  " +
            "    u.id,  " +
            "    u.email,  " +
            "    u.verified_phone,  " +
            "    u.status,  " +
            "    CONCAT_WS(' ', u.first_name, u.last_name) AS owner_name  " +
            "FROM  " +
            "    roles r  " +
            "        JOIN  " +
            "    model_has_roles mrh ON mrh.role_id = r.id  " +
            "        JOIN  " +
            "    users u ON mrh.model_id = u.id  " +
            "WHERE  " +
            "    (u.first_name LIKE CONCAT('%',:name,'%')  " +
            "        OR u.last_name LIKE CONCAT('%',:name,'%'))  " +
            "        AND u.deleted_at IS NULL  " +
            "        AND u.status = 1" +
            "        AND r.id IN (3)", nativeQuery = true)
    List<User> getUserSellerByName(@Param("name") String name);

    @Query(value = "SELECT   " +
            "    r.name AS user_type,  " +
            "    r.id AS role_id,  " +
            "    u.id,  " +
            "    u.email,  " +
            "    u.verified_phone,  " +
            "    u.status,  " +
            "    CONCAT_WS(' ', u.first_name, u.last_name) AS owner_name  " +
            "FROM  " +
            "    roles r  " +
            "        JOIN  " +
            "    model_has_roles mrh ON mrh.role_id = r.id  " +
            "        JOIN  " +
            "    users u ON mrh.model_id = u.id  " +
            "WHERE  " +
            "    (u.first_name LIKE CONCAT('%',:name,'%')  " +
            "        OR u.last_name LIKE CONCAT('%',:name,'%'))  " +
            "        AND u.deleted_at IS NULL  " +
            "        AND u.status = 1" +
            "        AND r.id IN (4)", nativeQuery = true)
    List<User> getUserBuyerByName(@Param("name") String name);

    @Modifying
    @Query(value = "update users set first_name = :first_name, " +
            "last_name = :last_name, " +
            "company = :company, " +
            "address_1 = :address, " +
            "postalcode = :postalcode, " +
            "city_id = :city_id, " +
            "state_id = :state_id, " +
            "country_id = :country_id, " +
            "updated_at = now(), " +
            "password = :password " +
            "where " +
            "id = :id", nativeQuery = true)
    @Deprecated
    void updateUserWithPassword(String first_name, String last_name, String company, String address, String postalcode, Integer city_id, Integer state_id, Integer country_id, String password, int id);

    @Query(value = "SELECT * FROM users where id = :id AND deleted_at IS NULL", nativeQuery = true)
    Optional<Users> findById(@Param("id") int id);

    @Query(value = "SELECT" +
            " count( id ) AS count " +
            "FROM " +
            " users s" +
            " JOIN model_has_roles mhr " +
            " ON s.id = mhr.model_id " +
            "WHERE " +
            " s.phone = :username " +
            " AND s.deleted_at IS NULL " +
            " AND s.STATUS = 1 " +
            " AND mhr.role_id IN (1,2)", nativeQuery = true)
    int isUsernameRegisteredAsAdmin(@Param("username") String username);
}
