package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
	public Page<Manager> findByNameContainsOrderByNameAsc(String mc,Pageable page);

}
