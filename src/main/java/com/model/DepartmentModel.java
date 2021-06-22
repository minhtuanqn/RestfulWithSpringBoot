package com.model;

import com.entity.DepartmentEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Component
public class DepartmentModel {
    private Integer id;

    @Pattern(regexp = "[a-zA-Z]*", message = "{name.pattern}")
    @NotNull(message = "{name.null}")
    @Length(min = 3, max = 50, message = "{name.length}")
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;

    public DepartmentModel() {
    }

    public DepartmentModel(Integer id, String name, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime deleteAt) {
        this.id = id;
        this.name = name;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.deleteAt = deleteAt;
    }

    public DepartmentModel(DepartmentEntity entity) {
        this.id = entity.getId();;
        this.name = entity.getName();;
        this.createAt = entity.getCreateAt();
        this.updateAt = entity.getUpdateAt();
        this.deleteAt = entity.getDeleteAt();
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

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
