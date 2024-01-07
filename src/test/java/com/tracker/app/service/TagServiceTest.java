package com.tracker.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.tracker.app.dao.TagRepository;
import com.tracker.app.entities.Tag;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

	@Mock
	TagRepository tagRepository;
	
	@InjectMocks
	TagService tagService;
	
	@Test
    void testGetAllTags() {
        List<Tag> tags = new ArrayList<>();
        // Add some sample tags to the list

        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> result = tagService.getTags();

        assertEquals(tags, result);
    }

    @Test
    void testGetTagsByName() {
        String keyword = "test";
        int page = 0;
        int size = 10;
        Page<Tag> tagsPage = mock(Page.class);

        when(tagRepository.findByNameContainsOrderByNameAsc(keyword, PageRequest.of(page, size))).thenReturn(tagsPage);

        Page<Tag> result = tagService.getTags(keyword, page, size);

        assertEquals(tagsPage, result);
    }

    @Test
    void testGetOneTag() {
        Long tagId = 1L;
        Tag tag = new Tag();
        tag.setId(tagId);

        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        Tag result = tagService.oneTag(tagId);

        assertEquals(tag, result);
    }

    @Test
    void testGetOneTagNotFound() {
        Long tagId = 1L;

        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

        Tag result = tagService.oneTag(tagId);

        assertNull(result);
    }

    @Test
    void testCreateOrUpdateTag() {
        Tag tag = new Tag();

        tagService.createOrUpdate(tag);

        verify(tagRepository).save(tag);
    }

    @Test
    void testDeleteTag() {
        Long tagId = 1L;

        tagService.delete(tagId);

        verify(tagRepository).deleteById(tagId);
    }
	
}
