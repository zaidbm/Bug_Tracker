package com.tracker.app.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Bug;
import com.tracker.app.entities.Category;
import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Priority;
import com.tracker.app.entities.Severity;

public interface BugRepository extends JpaRepository<Bug,Long> {
	public Page<Bug> findByTitleContainsOrderByTitleAsc(String mc,Pageable page);
	public Page<Bug> findByCategory(Category category,Pageable page);
	public Page<Bug> findByPriority(Priority priority,Pageable page);
	public Page<Bug> findBySeverity(Severity severity,Pageable page);
	public Page<Bug> findByPlatform(String platform,Pageable page);
	public Page<Bug> findByStatus(String status,Pageable page);
	public Page<Bug> findByBugCreator(Employe bugCreator,Pageable page);
	public long count();
	public long countByStatus(String Status);
	public Long countByCategory(Category category);
}

