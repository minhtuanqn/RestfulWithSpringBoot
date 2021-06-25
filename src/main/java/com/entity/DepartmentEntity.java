package com.entity;

import com.model.DepartmentModel;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "department")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departmentEntity")
    private Set<StaffEntity> staffList;

    public DepartmentEntity() {
    }

    public DepartmentEntity(Integer id, String name, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime deleteAt) {
        this.id = id;
        this.name = name;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.deleteAt = deleteAt;
    }

    public DepartmentEntity(DepartmentModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.createAt = model.getCreateAt();
        this.updateAt = model.getUpdateAt();
        this.deleteAt = model.getDeleteAt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<StaffEntity> getStaffList() {
        return staffList;
    }

    public void setStaffList(Set<StaffEntity> staffList) {
        this.staffList = staffList;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }


}
