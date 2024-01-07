package com.tracker.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.entities.Priority;

@Service
public class PriorityService {
	private PriorityRepository priorityRepository;
	public PriorityService() {}
	@Autowired
	public PriorityService(PriorityRepository priorityRepository) {
		this.priorityRepository=priorityRepository;
	}
	public Page<Priority> getPriorities(String mc,int page, int size){
		return priorityRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));	
	}
	public Priority getPriorityById(Long id) {
		Optional<Priority> priority=priorityRepository.findById(id);
		return priority.orElse(null);
	}
	public void saveOrUpdate(Priority priority) {
		priorityRepository.save(priority);
	}
	public void delete(Long id) {
		priorityRepository.deleteById(id);
	}
	public List<Priority> getPriorities() {
		return priorityRepository.findAll();
	}
	public Priority findByName(String Priority) {
		return priorityRepository.findByName(Priority);
	}
	
	
}
