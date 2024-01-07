package com.tracker.app.entities;

import java.util.Set;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue(value="developer")
public class Developer extends Employe{
	@ManyToMany(targetEntity = Team.class)
	@JoinTable(
			  name = "developer_teams", 
			  joinColumns = @JoinColumn(name = "id_developer"), 
			  inverseJoinColumns = @JoinColumn(name = "id_team"))
	private Set<Team> developerTeams;
	
	@ManyToMany(targetEntity = Project.class)
	@JoinTable(
			  name = "developer_projects", 
			  joinColumns = @JoinColumn(name = "id_developer"), 
			  inverseJoinColumns = @JoinColumn(name = "id_Project"))
	private Set<Project> developerProjects;
	
	public Developer() {
	}

	public Developer(String name, String email, Set<Bug> bugs, Set<Comment> comments,Set<Team> developerTeams,Set<Project> developerProjects,
			Bug bug) {
		super(name, email, bugs, comments, bug);
		this.developerTeams = developerTeams;
		this.developerProjects=developerProjects;
	}

	public Set<Team> getDeveloperTeams() {
		return developerTeams;
	}

	public void setDeveloperTeams(Set<Team> developerTeams) {
		this.developerTeams = developerTeams;
	}
	@Override
	public String toString() {
		return this.getName();
	}

	public Set<Project> getDeveloperProjects() {
		return developerProjects;
	}

	public void setDeveloperProjects(Set<Project> developerProjects) {
		this.developerProjects = developerProjects;
	}
	
}
