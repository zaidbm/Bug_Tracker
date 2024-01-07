package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Severity;

public interface SeverityRepository extends JpaRepository<Severity, Long>{
	public Page<Severity> findByNameContainsOrderByNameAsc(String mc,Pageable page);
	public Long countByName(String Severity);
}
