package com.model;

import com.entity.StaffEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * Staff model
 */
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffModel {

    private Integer id;

    @Pattern(regexp = "[a-zA-Z]*", message = "Just use character for first name")
    @NotNull(message = "First name must be not null")
    @Length(min = 3, max = 50, message = "Length of first name must be from3-50 characters")
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]*", message = "Just use character for last name")
    @NotNull(message = "Last name must be not null")
    @Length(min = 3, max = 50, message = "Length of last name must be from3-50 characters")
    private String lastName;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    public StaffModel() {
    }

    public StaffModel(StaffEntity staffEntity) {
        this.id = staffEntity.getId();
        this.firstName = staffEntity.getFirstName();
        this.lastName = staffEntity.getLastName();
    }

    public StaffModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

}
