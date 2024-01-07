package com.tracker.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.entities.Category;
import com.tracker.app.entities.Severity;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

	@InjectMocks
	CategoryService categoryService;
	@Mock
	CategoryRepository categoryRepository;
	
	 @Test
	    public void getCategoriesTest() {
	        // Mock the behavior of the categoryRepository
		 	String keyWord="Cat";
	        List<Category> categories = new ArrayList<>();
	        categories.add(new Category("Category1"));
	        categories.add(new Category("Category2"));
	        Page<Category> categoryPage = new PageImpl<>(categories);
	        when(categoryRepository.findByNameContainsOrderByNameAsc(eq(keyWord),eq(PageRequest.of(0, 10))))
	        .thenReturn(categoryPage);
	        
	        Page<Category> result = categoryService.getCategories(keyWord, 0, 10);

	        // Verify the result
	        assertEquals(categoryPage, result);
	        verify(categoryRepository).findByNameContainsOrderByNameAsc(eq(keyWord),eq(PageRequest.of(0, 10)));
	    }
	 
	 
	 @Test
	    void testGetCategoryById() {
	        // Arrange
	        Long categoryId = 1L;
	        Category category = new Category("TestCategory");
	        category.setId(categoryId);

	        // Mock the behavior of the categoryRepository
	        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

	        // Act
	        Category result = categoryService.getCategoryById(categoryId);

	        // Assert
	        assertNotNull(result);
	        assertEquals(category, result);

	        // Verify that the findById method was called with the correct argument
	        verify(categoryRepository).findById(categoryId);
	    }

	    @Test
	    void testGetCategoryByIdNotFound() {
	        // Arrange
	        Long categoryId = 1L;

	        // Mock the behavior of the categoryRepository when the category is not found
	        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

	        // Act
	        Category result = categoryService.getCategoryById(categoryId);

	        // Assert
	        assertNull(result);

	        // Verify that the findById method was called with the correct argument
	        verify(categoryRepository).findById(categoryId);
	    }
	 
	    @Test
	    void testSaveOrUpdate() {
	        // Arrange
	        Category categoryToSave = new Category("Category");

	        // Act
	        categoryService.saveOrUpdate(categoryToSave);

	        // Assert
	        // Verify that the save method was called with the correct argument
	        verify(categoryRepository).save(categoryToSave);
	    }
	 
	    
	    @Test
	    void testDelete() {
	        // Arrange
	        Long categoryId = 1L;

	        // Act
	        categoryService.delete(categoryId);

	        // Assert
	        // Verify that the deleteById method was called with the correct argument
	        verify(categoryRepository).deleteById(categoryId);
	    }

	    @Test
	    void testGetSeverities() {
	        // Arrange
	        List<Category> expectedcategories = new ArrayList<>();
	        expectedcategories.add(new Category("Cat1"));
	        expectedcategories.add(new Category("Cat2"));
	        expectedcategories.add(new Category("Cat3"));

	        // Mock the behavior of the severityRepository
	        when(categoryRepository.findAll()).thenReturn(expectedcategories);

	        // Act
	        List<Category> result = categoryService.getCategories();

	        // Assert
	        assertNotNull(result);
	        assertEquals(expectedcategories.size(), result.size());
	        assertEquals(expectedcategories, result);

	        // Verify that the findAll method was called
	        verify(categoryRepository).findAll();
	    }

	}

