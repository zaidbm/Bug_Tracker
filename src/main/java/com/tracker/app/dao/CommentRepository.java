package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.app.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	Page<Comment> findByBodyContainsOrderByBodyAsc(String mc,Pageable page);

}
