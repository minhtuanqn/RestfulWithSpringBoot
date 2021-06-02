package com.repository;

import com.entity.StaffEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface of method for service layer
 */
public interface DefaultRepository extends CrudRepository<StaffEntity, Integer>, JpaSpecificationExecutor<StaffEntity> {

}
