package com.repository;

import com.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Integer>, JpaSpecificationExecutor<DepartmentEntity> {

    boolean existsDepartmentEntitiesByIdAndDeleteAtNull(Integer id);

    boolean existsDepartmentEntitiesByName(String name);

    boolean existsDepartmentEntitiesByNameAndIdNot(String name, Integer id);

    @Query(value = "select * from department where id = ?1 and delete_at is null", nativeQuery = true)
    Optional<DepartmentEntity> findDepartmentById(Integer id);
    
}
