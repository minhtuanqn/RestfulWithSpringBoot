package com.security;

import com.entity.StaffEntity;
import com.model.StaffModel;
import com.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class StaffDetailsService implements UserDetailsService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<StaffEntity> staffEntityOptional  = staffRepository.findStaffEntityByUsername(username);
        StaffEntity staffEntity = staffEntityOptional.orElseThrow();
        return new StaffDetails(new StaffModel(staffEntity));
    }
}
