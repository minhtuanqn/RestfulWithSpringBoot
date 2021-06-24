package com.security;

import com.model.StaffModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class StaffDetails implements UserDetails {

    private StaffModel staffModel;

    public StaffDetails(StaffModel staffModel) {
        this.staffModel = staffModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return staffModel.getPassword();
    }

    @Override
    public String getUsername() {
        return staffModel.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return staffModel.getDeleteAt() == null;
    }
}
