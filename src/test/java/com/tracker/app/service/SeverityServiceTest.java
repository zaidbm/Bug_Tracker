package com.tracker.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.entities.Category;
import com.tracker.app.entities.Severity;

@ExtendWith(MockitoExtension.class)
public class SeverityServiceTest {
	
	@Mock
	SeverityRepository severityRepository;
	
	@InjectMocks
	SeverityService severityService;
	
	@Test
    public void getSeveritiesTest() {
        // Mock the behavior of the categoryRepository
	 	String keyWord="Sev";
        List<Severity> severities = new ArrayList<>();
        severities.add(new Severity("Severity1"));
        severities.add(new Severity("Severity2"));
        Page<Severity> expectedSeverity = new PageImpl<>(severities);
        when(severityRepository.findByNameContainsOrderByNameAsc(eq(keyWord),eq(PageRequest.of(0, 10))))
        .thenReturn(expectedSeverity);
        
        Page<Severity> ActualResults = severityService.getSeverities(keyWord, 0, 10);

        // Verify the result
        assertEquals(expectedSeverity, ActualResults);
        verify(severityRepository).findByNameContainsOrderByNameAsc(eq(keyWord),eq(PageRequest.of(0, 10)));
    }
 
 
 @Test
    void testGetSeverityById() {
        // Arrange
        Long severityId = 1L;
        Severity severity = new Severity("TestSeverity");
        Optional<Severity> optionalSeverity= Optional.of(severity);

        // Mock the behavior of the categoryRepository
        when(severityRepository.findById(severityId)).thenReturn(optionalSeverity);

        // Act
        Severity result = severityService.getSeverityById(severityId);

        // Assert
        assertNotNull(result);
        assertEquals(severity, result);

        // Verify that the findById method was called with the correct argument
        verify(severityRepository).findById(severityId);
    }

    @Test
    void testGetSeverityByIdNotFound() {
        // Arrange
        Long severityId = 2L;

        // Mock the behavior of the categoryRepository when the category is not found
        when(severityRepository.findById(severityId)).thenReturn(Optional.empty());

        // Act
        Severity result = severityService.getSeverityById(severityId);

        // Assert
        assertNull(result);

        // Verify that the findById method was called with the correct argument
        verify(severityRepository).findById(severityId);
    }
 
    @Test
    void testSaveOrUpdate() {
        // Arrange
        Severity severityToSave = new Severity("Severity");

        // Act
        severityService.saveOrUpdate(severityToSave);

        // Assert
        // Verify that the save method was called with the correct argument
        verify(severityRepository).save(severityToSave);
    }
 
    
    
    @Test
    void testDelete() {
        // Arrange
        Long severityId = 1L;

        // Act
        severityService.delete(severityId);

        // Assert
        // Verify that the deleteById method was called with the correct argument
        verify(severityRepository).deleteById(severityId);
    }
    
    @Test
    void testGetSeverities() {
        // Arrange
        List<Severity> expectedSeverities = new ArrayList<>();
        expectedSeverities.add(new Severity("Low"));
        expectedSeverities.add(new Severity("Medium"));
        expectedSeverities.add(new Severity("High"));

        // Mock the behavior of the severityRepository
        when(severityRepository.findAll()).thenReturn(expectedSeverities);

        // Act
        List<Severity> result = severityService.getSeverities();

        // Assert
        assertNotNull(result);
        assertEquals(expectedSeverities.size(), result.size());
        assertEquals(expectedSeverities, result);

        // Verify that the findAll method was called
        verify(severityRepository).findAll();
    }
	
	
	
	
}
