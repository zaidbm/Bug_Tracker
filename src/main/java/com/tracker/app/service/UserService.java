package com.tracker.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import com.tracker.app.dao.RoleRepository;
import com.tracker.app.dao.UserRepository;
import com.tracker.app.entities.User;
import com.tracker.app.entities.Role;
import java.util.HashSet;

public class UserService {
	private UserRepository userRepository;
    private RoleRepository roleRepository;
    

    /*@Autowired
    private PasswordEncoder passwordEncoder;*/
	
	public UserService() {}
	@Autowired
	public UserService(UserRepository userRepository,RoleRepository roleRepository) {
		this.userRepository=userRepository;
		this.roleRepository=roleRepository;

	}

    public void save(User user) {
    	/*if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (passwordEncoder == null) {
            throw new IllegalStateException("passwordEncoder is null");
        }

        if (roleRepository == null) {
            throw new IllegalStateException("roleRepository is null");
        }

        if (userRepository == null) {
            throw new IllegalStateException("userRepository is null");
        }*/
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepository.findByName("DEVELOPER"));
        user.setRoles(defaultRoles);
        
        userRepository.save(user);
    }
    
    public Long getUserId(String username) {
    	User user= userRepository.findByUsername(username);
    	return user.getId();
    }
}
