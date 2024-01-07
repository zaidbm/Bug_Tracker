package com.tracker.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.entities.Category;

@Service
public class CategoryService {
	private CategoryRepository categoryRepository;
	public CategoryService() {}
	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository=categoryRepository;
	}
	public Page<Category> getCategories(String mc,int page, int size){
		return categoryRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public Category getCategoryById(Long id) {
		Optional<Category> category=categoryRepository.findById(id);
		return category.orElse(null);
	}
	public void saveOrUpdate(Category category) {
		categoryRepository.save(category);
	}
	public void delete(Long id) {
		categoryRepository.deleteById(id);
	}
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}
	
}
