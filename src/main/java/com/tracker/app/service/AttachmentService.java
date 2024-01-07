package com.tracker.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tracker.app.dao.AttachmentRepository;
import com.tracker.app.entities.Attachment;

@Service
public class AttachmentService {
	private AttachmentRepository attachmentRepository;
	public AttachmentService() {}
	@Autowired
	public AttachmentService(AttachmentRepository attachmentRepository) {
		this.attachmentRepository=attachmentRepository;
	}
	public List<Attachment> getAttachments() {
		return attachmentRepository.findAll();
	}
	public Page<Attachment> getAttachments(String mc,int page, int size){
		return attachmentRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public Attachment oneAttachment(Long id) {
		Optional<Attachment> attachment=attachmentRepository.findById(id);
		return attachment.orElse(null);
	}
	public void createOrUpdate(Attachment Attachement) {
		attachmentRepository.save(Attachement);
	}
	public void delete(Long id) {
		attachmentRepository.deleteById(id);
	}
}
