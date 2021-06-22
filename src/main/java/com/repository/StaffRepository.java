package com.repository;

import com.entity.StaffEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Interface of method for service layer
 */
public interface StaffRepository extends CrudRepository<StaffEntity, Integer>, JpaSpecificationExecutor<StaffEntity> {

    Page<StaffEntity> findStaffEntitiesByDepartmentEntityIdEquals(Integer id, Pageable pageable);

    boolean existsStaffEntitiesByUsername(String username);

    Optional<StaffEntity> findStaffEntityByIdAndDeleteAtNull(Integer id);

    boolean existsStaffEntityByIdNotAndUsernameEquals(Integer id, String username);

//    @Query(value = "SELECT * FROM staff s WHERE " +
//            "EXISTS (SELECT id FROM department d WHERE d.id = s.dep_id " +
//            " AND (s.first_name like :searchedValue or s.last_name like :searchedValue) " +
//            " and d.delete_at = null and s.delete_at = null)", nativeQuery = true)
//    Page<StaffEntity> searchExistLikeFirstNameAndLastName(@Param("searchedValue") String searchValue);
}
