package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.app.entities.CustomerSupport;

public interface CustomerSupportRepository extends JpaRepository<CustomerSupport, Long>{
	public Page<CustomerSupport> findByNameContainsOrderByNameAsc(String mc,Pageable page);

}
