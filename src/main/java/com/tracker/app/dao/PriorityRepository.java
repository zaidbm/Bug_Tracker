package com.tracker.app.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.tracker.app.entities.Priority;

public interface PriorityRepository extends JpaRepository<Priority,Long>{
	public Page<Priority> findByNameContainsOrderByNameAsc(String mc,Pageable page);
	public Priority findByName(String priority);
	public List<Priority> findAll();
	public Long countByName(String Priority);
}
