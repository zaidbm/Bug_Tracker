package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Employe;

public interface EmployeRepository extends JpaRepository<Employe,Long>{
	public Page<Employe> findByNameContainsOrderByNameAsc(String mc,Pageable page);
	public Employe findByName(String name);
}
