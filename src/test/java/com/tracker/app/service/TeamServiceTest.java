package com.tracker.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.tracker.app.dao.TeamRepository;
import com.tracker.app.entities.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
		
	@Mock
	TeamRepository teamRepository;
	
	@InjectMocks
	TeamService teamService;
	
	@Test
    void testGetTeamsByName() {
        String keyword = "test";
        int page = 0;
        int size = 10;
        List<Team> teams=new ArrayList<>();
        teams.add(new Team("team1"));
        teams.add(new Team("team2"));
        Page<Team> teamsPage = new PageImpl<>(teams);

        when(teamRepository.findByNameContainsOrderByNameAsc(keyword, PageRequest.of(page, size))).thenReturn(teamsPage);

        Page<Team> result = teamService.getTeams(keyword, page, size);

        assertEquals(teamsPage, result);
    }

    @Test
    void testGetAllTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("team1"));
        teams.add(new Team("team2"));
        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.getTeams();

        assertEquals(teams, result);
    }

    @Test
    void testGetOneTeam() {
        Long teamId = 1L;
        Team team = new Team();
        team.setId(teamId);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        Team result = teamService.oneTeam(teamId);

        assertEquals(team, result);
    }

    @Test
    void testGetOneTeamNotFound() {
        Long teamId = 1L;

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        Team result = teamService.oneTeam(teamId);

        assertNull(result);
    }

    @Test
    void testSaveOrUpdateTeam() {
        Team team = new Team();

        teamService.saveOrUpdate(team);

        verify(teamRepository).save(team);
    }

    @Test
    void testDeleteTeam() {
        Long teamId = 1L;

        teamService.delete(teamId);

        verify(teamRepository).deleteById(teamId);
    }

    @Test
    void testGetTeamsById() {
        Long[] teamIds = {1L, 2L, 3L};
        Team team1 = new Team("team1");
        team1.setId(1L);
        Team team2 = new Team("team2");
        team2.setId(2L);
        Team team3 = new Team("team3");
        team3.setId(3L);
        
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team1));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(team2));
        when(teamRepository.findById(3L)).thenReturn(Optional.of(team3));
        
        Set<Team> result = teamService.getTeamsById(teamIds);

        assertEquals(new HashSet<>(Arrays.asList(team1, team2, team3)), result);
    }
    @Test
    void testgetTeamsById_handlesEmptyInput() {
        // Exercise
        Set<Team> actualTeams = teamService.getTeamsById(new Long[] {});

        // Verify
        assertEquals(new HashSet<>(), actualTeams);
    }

    
}
