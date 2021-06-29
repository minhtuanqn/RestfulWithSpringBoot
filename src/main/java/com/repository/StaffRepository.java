package com.repository;

import com.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Interface of method for service layer
 */
public interface StaffRepository extends CrudRepository<StaffEntity, Integer>, JpaSpecificationExecutor<StaffEntity> {

    boolean existsStaffEntitiesByUsername(String username);

    boolean existsStaffEntityByIdNotAndUsernameEquals(Integer id, String username);

    Optional<StaffEntity> findStaffEntityByUsername(String username);

    @Query(value = "select * from staff where id = ?1 and delete_at is null", nativeQuery = true)
    Optional<StaffEntity> findStaffById(Integer id);

//    @Query(value = "select * from staff where id != ?1 and username = ?2", nativeQuery = true)
//    Optional<StaffEntity> findEntityWithSameUsername(Integer id, String username);

}
