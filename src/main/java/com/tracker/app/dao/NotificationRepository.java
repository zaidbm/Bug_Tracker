package com.tracker.app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tracker.app.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	//public Page<Notification> findByNameContainsOrderByNameAsc(String mc,Pageable page);
}
