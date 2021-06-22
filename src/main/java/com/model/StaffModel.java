package com.model;

import com.entity.StaffEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Staff model
 */
@Component
public class StaffModel {

    private Integer id;

    @Pattern(regexp = "[a-zA-Z]*", message = "{firstname.pattern}")
    @NotNull(message = "{firstname.null}")
    @Length(min = 3, max = 50, message = "{firstname.length}")
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]*", message = "{lastname.pattern}")
    @NotNull(message = "{lastname.null}")
    @Length(min = 3, max = 50, message = "{lastname.length}")
    private String lastName;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deleteAt;

    @NotNull(message = "{username.null}")
    @Length(min = 3, max = 50, message = "{username.length}")
    private String username;

    @NotNull(message = "{password.null}")
    @Length(min = 3, max = 50, message = "{password.length}")
    private String password;

    @Min(value = 1, message = "{depId.length}")
    @NotNull(message = "{depId.null}")
    private Integer depId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DepartmentModel departmentModel;

    public StaffModel() {
    }

    public StaffModel(StaffEntity staffEntity, DepartmentModel departmentModel) {
        this.id = staffEntity.getId();
        this.firstName = staffEntity.getFirstName();
        this.lastName = staffEntity.getLastName();
        this.createAt = staffEntity.getCreateAt();
        this.updatedAt = staffEntity.getUpdateAt();
        this.departmentModel = departmentModel;
        this.username = staffEntity.getUsername();
        this.password = staffEntity.getPassword();
        this.deleteAt = staffEntity.getDeleteAt();
    }

    public StaffModel(StaffEntity staffEntity) {
        this.id = staffEntity.getId();
        this.firstName = staffEntity.getFirstName();
        this.lastName = staffEntity.getLastName();
        this.createAt = staffEntity.getCreateAt();
        this.updatedAt = staffEntity.getUpdateAt();
        this.username = staffEntity.getUsername();
        this.password = staffEntity.getPassword();
        this.deleteAt = staffEntity.getDeleteAt();
    }

    public StaffModel(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DepartmentModel getDepartmentModel() {
        return departmentModel;
    }

    public void setDepartmentModel(DepartmentModel departmentModel) {
        this.departmentModel = departmentModel;
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

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }
}
