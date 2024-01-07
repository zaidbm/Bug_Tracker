package com.tracker.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.entities.Severity;

@Service
public class SeverityService {
	private SeverityRepository severityRepository;
	public SeverityService() {}
	@Autowired
	public SeverityService(SeverityRepository severityRepository) {
		this.severityRepository=severityRepository;
	}
	public Page<Severity> getSeverities(String mc,int page, int size){
		return severityRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public Severity getSeverityById(Long id) {
		Optional<Severity> severity=severityRepository.findById(id);
		return severity.orElse(null);
	}
	public void saveOrUpdate(Severity severity) {
		severityRepository.save(severity);
	}
	public void delete(Long id) {
		severityRepository.deleteById(id);
	}
	public List<Severity> getSeverities() {
		return severityRepository.findAll();
	}
}
