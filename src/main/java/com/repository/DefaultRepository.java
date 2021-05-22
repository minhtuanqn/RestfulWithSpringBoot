package com.repository;

import com.entity.StaffEntity;
import com.model.StaffModel;
import org.springframework.data.domain.Sort;
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

    @Query(value = "select id, first_name, last_name from Staff where last_name = ?1", nativeQuery = true)
    List<StaffEntity> findByLastName(String lastName);


}
