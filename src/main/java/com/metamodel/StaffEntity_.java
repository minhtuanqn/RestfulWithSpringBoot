package com.metamodel;

import com.entity.DepartmentEntity;
import com.entity.StaffEntity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

/**
 * Metamodel for staff entity
 */
@StaticMetamodel(StaffEntity.class)
public class StaffEntity_ {
    public static volatile SingularAttribute<StaffEntity, Integer> id;
    public static volatile SingularAttribute<StaffEntity, String> firstName;
    public static volatile SingularAttribute<StaffEntity, String> lastName;
    public static volatile SingularAttribute<StaffEntity, LocalDateTime> createAt;
    public static volatile SingularAttribute<StaffEntity, LocalDateTime> updateAt;
    public static volatile SingularAttribute<StaffEntity, LocalDateTime> deleteAt;
    public static volatile SingularAttribute<StaffEntity, DepartmentEntity> departmentEntity;
    public static final String ID = "id";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String CREATE_AT = "createAt";
    public static final String DELETE_AT = "deleteAt";
    public static final String UPDATE_AT = "updateAt";
    public static final String DEP_ID = "departmentEntity";

}
