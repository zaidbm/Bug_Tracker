package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Tag;

public interface TagRepository extends JpaRepository<Tag,Long>{
	public Page<Tag> findByNameContainsOrderByNameAsc(String mc,Pageable page);
}
