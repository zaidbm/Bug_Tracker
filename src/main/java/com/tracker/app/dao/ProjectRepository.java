package com.tracker.app.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> {
	public Page<Project> findByTitleContainsOrderByTitleAsc(String mc,Pageable page);
	public List<Project> findByTeamIsNull();
	public long count();
	public Long countByStatus(String Status);
}
