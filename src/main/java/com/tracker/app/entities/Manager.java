package com.tracker.app.entities;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@DiscriminatorValue(value="manager")
public class Manager extends Employe {
	@ManyToMany(targetEntity = Team.class)
	@JoinTable(
			  name = "manager_teams", 
			  joinColumns = @JoinColumn(name = "id_manager"), 
			  inverseJoinColumns = @JoinColumn(name = "id_team"))
	private Set<Team> teamsManaged=new HashSet<Team>();
	@ManyToMany(targetEntity = Project.class)
	@JoinTable(
			  name = "manager_projects", 
			  joinColumns = @JoinColumn(name = "id_manager"), 
			  inverseJoinColumns = @JoinColumn(name = "id_Project"))
	private Set<Project> managerProjects;
	
	public Manager() {
	}
	public Manager(String name, String email, Set<Bug> bugs, Set<Comment> comments,
			Set<Team> teamsManaged, Set<Project> manaProjects,Bug bug) {
		super(name, email, bugs, comments, bug);
		this.teamsManaged = teamsManaged;
		this.managerProjects = manaProjects;
	}
	
	public Set<Team> getTeamsManaged() {
		return teamsManaged;
	}
	public void setTeamsManaged(Set<Team> teamsManaged) {
		this.teamsManaged = teamsManaged;
	}
	
	
	
	public Set<Project> getManagerProjects() {
		return managerProjects;
	}
	public void setManagerProjects(Set<Project> managerProjects) {
		this.managerProjects = managerProjects;
	}
	@Override
	public String toString() {
		return  getName();
	}
	//set all occurences of team in project table to null 
	/*@PreRemove
	public void setIdManagerInProjectToNull() {
		for (Project project : managerProjects) {
			project.setManager(null);
		}
	}*/
}
