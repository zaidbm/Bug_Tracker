package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.app.entities.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long>{
	public Page<Developer> findByNameContainsOrderByNameAsc(String mc,Pageable page);

}
