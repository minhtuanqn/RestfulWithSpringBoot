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

    Optional<StaffEntity> findStaffEntityByUsername(String username);

}
