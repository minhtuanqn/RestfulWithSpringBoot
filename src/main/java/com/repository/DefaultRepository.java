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

/**
 * Interface of method for service layer
 */
public interface DefaultRepository extends CrudRepository<StaffEntity, Integer>, JpaSpecificationExecutor<StaffEntity> {

    /**
     * Get all staff
     * @param pageable
     * @return
     */
    @Query(value = "select id, first_name, last_name from Staff", nativeQuery = true)
    List<StaffEntity>  getAll(Pageable pageable);

}
