package com.tracker.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);
}
