package com.tracker.app.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.entities.Category;
import com.tracker.app.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategorycontrollerTest {
	
	@Mock
	CategoryService categoryService;
	
	@InjectMocks
	CategoryController categoryController=new CategoryController();
	
	@Mock
    Model model;
	
	@Mock
	Errors errors;
	@Test
    void testIndex() {
        // Arrange
        String mc = "test";
        int page = 0;
        int size = 5;
        List<Category> categories = new ArrayList<>();  // Add actual categories
        categories.add(new Category("ddd"));
        Page<Category> mockCategoryPage = new PageImpl<>(categories);

        when(categoryService.getCategories(mc, page, size)).thenReturn(mockCategoryPage);


        // Act
        String destination = categoryController.Index(mc, page, size, model);

        // Assert
        assertEquals("Admin/Category/Categories", destination);
        verify(model).addAttribute("mc", mc);
        verify(model).addAttribute("cats", mockCategoryPage.getContent());
        verify(model).addAttribute("pages", new int[mockCategoryPage.getTotalPages()]);
        verify(model).addAttribute("currentPage", page);

        // You can also verify interactions with the mocked categoryService if needed
        verify(categoryService).getCategories(mc, page, size);
    }	

	@Test
    void testCreate() {
        // Arrange

        // Act
        String redirect = categoryController.create(model);

        // Assert
        assertEquals("Admin/Category/Create", redirect);

        // Verify that the model attributes are set correctly
        verify(model).addAttribute(eq("category"), any(Category.class));
    }
	
	
	@Test
    void testStoreWithValidationErrors() {
        // Arrange
        Category category = new Category("ddd");  // Add relevant data to the category
        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        // Act
        String destination = categoryController.Store(category, errors);

        // Assert
        assertEquals("Admin/category/Create", destination);
        // Verify that saveOrUpdate is not called when there are validation errors
        verify(categoryService, never()).saveOrUpdate(category);
    }
	
	@Test
    void testStoreWithoutValidationErrors() {
        // Arrange
        Category category = new Category("fff");  // Add relevant data to the category
        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(false);

        // Act
        String destination = categoryController.Store(category, errors);

        // Assert
        assertEquals("redirect:Index", destination);
        // Verify that saveOrUpdate is called when there are no validation errors
        verify(categoryService,times(1)).saveOrUpdate(category);
    }
	
	@Test
    void testDelete() {
        // Arrange
        Long categoryId = 1L;

        // Act
        String destination = categoryController.Delete(categoryId);

        // Assert
        assertEquals("redirect:Index", destination);
        // Verify that the delete method of categoryService is called with the correct id
        verify(categoryService).delete(categoryId);
    }
	
	@Test
    void testEdit() {
        // Arrange
        Long categoryId = 1L;
        Category mockCategory = new Category();  // Create a mock category for testing
        when(categoryService.getCategoryById(categoryId)).thenReturn(mockCategory);

        // Act
        String destination = categoryController.Edit(categoryId, model);

        // Assert
        assertEquals("Admin/category/Edit", destination);
        // Verify that getCategoryById is called with the correct id
        verify(categoryService).getCategoryById(categoryId);
        // Verify that the category attribute is added to the model
        verify(model).addAttribute("category", mockCategory);
    }
	
	@Test
    void testUpdateWithValidationErrors() {
        // Arrange
        Category category = new Category();  // Add relevant data to the category
        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        // Act
        String destination = categoryController.update(category, errors);

        // Assert
        assertEquals("Admin/category/Edit", destination);
        // Verify that saveOrUpdate is not called when there are validation errors
        verify(categoryService, never()).saveOrUpdate(category);
    }

    @Test
    void testUpdateWithoutValidationErrors() {
        // Arrange
        Category category = new Category();  // Add relevant data to the category
        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(false);

        // Act
        String destination = categoryController.update(category, errors);

        // Assert
        assertEquals("redirect:Index", destination);
        // Verify that saveOrUpdate is called when there are no validation errors
        verify(categoryService).saveOrUpdate(category);
    }
	
	
	
	
	
	
}
/*
@Mock
private Errors errors;

@Test
void testUpdateWithValidCategory() {
    Category category = new Category(); // Create a valid category object for testing

    categoryController.update(category, errors);

    verify(errors, never()).hasErrors();
    verify(categoryService).saveOrUpdate(category);
}

@Test
void testUpdateWithInvalidCategory() {
    Category category = new Category(); // Create an invalid category object for testing

    when(errors.hasErrors()).thenReturn(true);

    String result = categoryController.update(category, errors);

    verify(errors).hasErrors();
    verify(categoryService, never()).saveOrUpdate(category);
    assertEquals("Admin/category/Edit", result);
}
*/