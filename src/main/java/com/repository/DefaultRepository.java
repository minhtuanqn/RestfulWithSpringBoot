package com.repository;

import com.entity.StaffEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interface of method for service layer
 */
public interface DefaultRepository extends CrudRepository<StaffEntity, Integer> {

    @Query("select s from StaffEntity s where s.firstName = :firstName")
    List<StaffEntity> findByFirstName(@Param("firstName") String firstName);


}
