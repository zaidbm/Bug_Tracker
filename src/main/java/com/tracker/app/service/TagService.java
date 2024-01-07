package com.tracker.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tracker.app.dao.TagRepository;
import com.tracker.app.entities.Tag;

@Service
public class TagService {
	private TagRepository tagRepository;
	public TagService() {}
	@Autowired
	public TagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}
	public List<Tag> getTags() {
		return tagRepository.findAll();
	}
	public Page<Tag> getTags(String mc,int page, int size){
		return tagRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public Tag oneTag(Long id) {
		Optional<Tag> tag=tagRepository.findById(id);
		return tag.orElse(null);
	}
	public void createOrUpdate(Tag tag) {
		tagRepository.save(tag);
	}
	public void delete(Long id) {
		tagRepository.deleteById(id);
	}
	
}
