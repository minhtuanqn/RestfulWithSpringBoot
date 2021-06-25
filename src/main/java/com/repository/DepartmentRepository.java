package com.repository;

import com.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Integer>, JpaSpecificationExecutor<DepartmentEntity> {

    boolean existsDepartmentEntitiesByIdAndDeleteAtNull(Integer id);

    boolean existsDepartmentEntitiesByName(String name);

    boolean existsDepartmentEntitiesByNameAndIdNot(String name, Integer id);

    Optional<DepartmentEntity> findDepartmentEntityByIdAndDeleteAtNull(Integer id);

    Page<DepartmentEntity> findDepartmentEntityByNameContainsAndDeleteAtNull(String name, Pageable pageable);
    
}
