package com.metamodel;

import com.entity.DepartmentEntity;

import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDateTime;

/**
 * Metamodel for department entity
 */
public class DepartmentEntity_ {
    public static volatile SingularAttribute<DepartmentEntity, Integer> id;
    public static volatile SingularAttribute<DepartmentEntity, String> name;
    public static volatile SingularAttribute<DepartmentEntity, LocalDateTime> createAt;
    public static volatile SingularAttribute<DepartmentEntity, LocalDateTime> updateAt;
    public static volatile SingularAttribute<DepartmentEntity, LocalDateTime> deleteAt;
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATE_AT = "createAt";
    public static final String UPDATE_AT = "updateAt";
    public static final String DELETE_AT = "deleteAt";
}
