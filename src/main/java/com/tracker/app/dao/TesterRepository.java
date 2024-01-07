package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.app.entities.Tester;

public interface TesterRepository extends JpaRepository<Tester,Long>{
	public Page<Tester> findByNameContainsOrderByNameAsc(String mc,Pageable page);

}
