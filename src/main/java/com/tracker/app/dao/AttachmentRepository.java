package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.app.entities.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

	Page<Attachment> findByNameContainsOrderByNameAsc(String mc, PageRequest of);

	//gg
//<hh
}
