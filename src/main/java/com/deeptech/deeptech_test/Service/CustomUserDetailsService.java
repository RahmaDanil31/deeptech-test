package com.deeptech.deeptech_test.Service;

import com.deeptech.deeptech_test.Model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminService adminService;

    @Autowired
    public CustomUserDetailsService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getByEmail(username);
        if (admin == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(admin);
    }
}
