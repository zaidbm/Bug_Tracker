package com.tracker.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.entities.Priority;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriorityServiceTest {
	
	@Mock
	PriorityRepository priorityRepository;
	
	@InjectMocks
	PriorityService priorityService;
	

	@Test
    public void getPrioritiesTest() {
        // Mock the behavior of the categoryRepository
	 	String keyWord="Prio";
        List<Priority> priorities = new ArrayList<>();
        priorities.add(new Priority("Priority1"));
        priorities.add(new Priority("Priority2"));
        Page<Priority> expectedPriority = new PageImpl<>(priorities);
        when(priorityRepository.findByNameContainsOrderByNameAsc(eq(keyWord),eq(PageRequest.of(0, 5))))
        .thenReturn(expectedPriority);
        
        Page<Priority> ActualResults = priorityService.getPriorities(keyWord, 0, 5);

        // Verify the result
        assertEquals(expectedPriority, ActualResults);
        verify(priorityRepository).findByNameContainsOrderByNameAsc(eq(keyWord),eq(PageRequest.of(0, 5)));
    }
	 @Test
	    void testGetpriorityById() {
	        // Arrange
	        Long priorityId = 1L;
	        Priority priority = new Priority("TestPriority");
	        Optional<Priority> optionalPriority= Optional.of(priority);

	        // Mock the behavior of the categoryRepository
	        when(priorityRepository.findById(priorityId)).thenReturn(optionalPriority);

	        // Act
	        Priority result = priorityService.getPriorityById(priorityId);

	        // Assert
	        assertNotNull(result);
	        assertEquals(priority, result);

	        // Verify that the findById method was called with the correct argument
	        verify(priorityRepository).findById(priorityId);
	    }

	    @Test
	    void testGetPriorityByIdNotFound() {
	        // Arrange
	        Long priorityId = 2L;

	        // Mock the behavior of the categoryRepository when the category is not found
	        when(priorityRepository.findById(priorityId)).thenReturn(Optional.empty());

	        // Act
	        Priority result = priorityService.getPriorityById(priorityId);

	        // Assert
	        assertNull(result);

	        // Verify that the findById method was called with the correct argument
	        verify(priorityRepository).findById(priorityId);
	    }
	 
	    @Test
	    void testSaveOrUpdate() {
	        // Arrange
	    	Priority severityToSave = new Priority("Severity");

	        // Act
	        priorityService.saveOrUpdate(severityToSave);

	        // Assert
	        // Verify that the save method was called with the correct argument
	        verify(priorityRepository).save(severityToSave);
	    }
	 
	    
	    
	    @Test
	    void testDelete() {
	        // Arrange
	        Long severityId = 1L;

	        // Act
	        priorityService.delete(severityId);

	        // Assert
	        // Verify that the deleteById method was called with the correct argument
	        verify(priorityRepository).deleteById(severityId);
	    }
	    
	    @Test
	    void testGetSeverities() {
	        // Arrange
	        List<Priority> expectedPriorities = new ArrayList<>();
	        expectedPriorities.add(new Priority("priority1"));
	        expectedPriorities.add(new Priority("priority2"));
	        expectedPriorities.add(new Priority("priority3"));

	        // Mock the behavior of the severityRepository
	        when(priorityRepository.findAll()).thenReturn(expectedPriorities);

	        // Act
	        List<Priority> result = priorityService.getPriorities();

	        // Assert
	        assertNotNull(result);
	        assertEquals(expectedPriorities.size(), result.size());
	        assertEquals(expectedPriorities, result);

	        // Verify that the findAll method was called
	        verify(priorityRepository).findAll();
	    }
		
		
		
		
	}

	

