package com.model;

import com.entity.StaffEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

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

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    public StaffModel() {
    }

    public StaffModel(StaffEntity staffEntity) {
        this.id = staffEntity.getId();
        this.firstName = staffEntity.getFirstName();
        this.lastName = staffEntity.getLastName();
        this.createAt = staffEntity.getCreateAt();
        this.updatedAt = staffEntity.getUpdateAt();
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
