package com.tracker.app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tracker.app.dao.CommentRepository;
import com.tracker.app.entities.Comment;

@Service
public class CommentService {
	CommentRepository commentRepository;
	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository=commentRepository;
	}
	public List<Comment> getComments() {
		return commentRepository.findAll();
	}
	public Page<Comment> getComments(String mc,int page, int size){
		return commentRepository.findByBodyContainsOrderByBodyAsc(mc,PageRequest.of(page,size));
	}
	public Comment oneComment(Long id) {
		return commentRepository.getById(id);
	}
	public void saveOrUpdate(Comment comment) {
		commentRepository.save(comment);
	}
	public void delete(Long id) {
		commentRepository.deleteById(id);
	}
}
