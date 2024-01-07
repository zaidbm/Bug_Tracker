package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Team;

public interface TeamRepository extends JpaRepository<Team,Long> {
	public Page<Team> findByNameContainsOrderByNameAsc(String mc,Pageable page);
	//public List<Team> findByProjectIdIsNull();
}
