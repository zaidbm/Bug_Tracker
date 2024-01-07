package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tracker.app.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	public Page<Category> findByNameContainsOrderByNameAsc(String mc,Pageable page);
	public Page<Category> findByname(String mc,Pageable page);
	public Long countByName(String category);
	public String findByname(String name);
}
