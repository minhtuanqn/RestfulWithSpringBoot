package com.entity;

import com.model.StaffModel;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Staff entity
 */
@Entity
@Table(name = "staff")
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dep_id", nullable = false)
    private DepartmentEntity departmentEntity;

    public StaffEntity() {
    }

    public StaffEntity(Integer id, String firstName, String lastName, LocalDateTime createAt,
                       LocalDateTime updateAt, DepartmentEntity departmentEntity,
                       String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.username = username;
        this.password = password;
        this.departmentEntity = departmentEntity;
    }

    public StaffEntity(StaffModel model, DepartmentEntity departmentEntity) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.updateAt = model.getUpdatedAt();
        this.createAt = model.getCreateAt();
        this.deleteAt = model.getDeleteAt();
        this.username = model.getUsername();
        this.password = model.getPassword();
        this.departmentEntity = departmentEntity;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DepartmentEntity getDepartmentEntity() {
        return departmentEntity;
    }

    public void setDepartmentEntity(DepartmentEntity departmentEntity) {
        this.departmentEntity = departmentEntity;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
