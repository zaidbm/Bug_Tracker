package com.tracker.app.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tracker.app.dao.ProjectRepository;
import com.tracker.app.entities.Project;
import com.tracker.app.entities.Team;

@Service
public class ProjectService {
	private ProjectRepository projectRepository;
	@Autowired
	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository=projectRepository;
	}
	public List<Project> getProjects() {
		return projectRepository.findAll();
	}
	public Page<Project> getProjects(String mc,int page, int size){
		return projectRepository.findByTitleContainsOrderByTitleAsc(mc,PageRequest.of(page,size));
	}
	public Project oneProject(Long id) {
		return projectRepository.getById(id);
	}
	public void saveOrUpdate(Project project) {
		projectRepository.save(project);
	}
	public void delete(Long id) {
		projectRepository.deleteById(id);
	}
	public List<Project> getProjectsWhereTeamIsNull() {
		return projectRepository.findByTeamIsNull();
	}
	public long Count(){
		return projectRepository.count();
	}
	public long CountByStatus(String Status){
		return projectRepository.countByStatus(Status);
	}
	public List<Project> ProjectWithMostBugs(){
		List<Project> projects= getProjects();
		projects.sort(Comparator.comparingLong(project -> ((Project) project).getBugs().size()).reversed());	
		return  projects;
	}
	
	public Set<Project> getProjectsById(Long[] projectID) {
		Set<Project> projects=new HashSet<Project>();
		for (Long id : projectID) {
			projects.add(oneProject(id));
		}
		return projects;
	}
	
	
}
