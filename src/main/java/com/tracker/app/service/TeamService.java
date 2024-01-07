package com.tracker.app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.tracker.app.dao.TeamRepository;
import com.tracker.app.entities.Team;

@Service
public class TeamService {
	private TeamRepository teamRepository;
	public TeamService() {}
	@Autowired
	public TeamService(TeamRepository teamRepository) {
		this.teamRepository=teamRepository;
	}
	public Page<Team> getTeams(String mc,int page, int size){
		return teamRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public List<Team> getTeams(){
		return teamRepository.findAll();
	}
	public Team oneTeam(Long id) {
		Optional<Team> team=teamRepository.findById(id);
		return team.orElse(null);
	}
	public void saveOrUpdate(Team team) {
		teamRepository.save(team);
	}
	public void delete(Long id) {
		teamRepository.deleteById(id);
	}
	public Set<Team> getTeamsById(Long[] teamsID) {
		Set<Team> teams=new HashSet<Team>();
		for (Long id : teamsID) {
			teams.add(oneTeam(id));
		}
		return teams;
	}
	/*public List<Team> getTeamsNotAssignedToAProject(){
		return teamRepository.findByProjectIdIsNull();
	}*/
}
