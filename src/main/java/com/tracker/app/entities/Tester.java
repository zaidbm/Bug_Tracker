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
@DiscriminatorValue(value="tester")
public class Tester extends Employe{
	@ManyToMany(targetEntity = Team.class)
	@JoinTable(
			  name = "developer_teams", 
			  joinColumns = @JoinColumn(name = "id_developer"), 
			  inverseJoinColumns = @JoinColumn(name = "id_team"))
	private Set<Team> TesterTeams=new HashSet<Team>();
	@ManyToMany(targetEntity = Project.class)
	@JoinTable(
			  name = "tester_projects", 
			  joinColumns = @JoinColumn(name = "id_tester"), 
			  inverseJoinColumns = @JoinColumn(name = "id_Project"))
	private Set<Project> testerProjects;
	public Tester() {
	}

	public Tester(String name, String email, Set<Bug> bugs, Set<Comment> comments,Set<Team> testerTeams,Set<Project> testerProjects,Bug bug) {
		super(name, email, bugs, comments, bug);
		this.TesterTeams=testerTeams;
		this.testerProjects=testerProjects;
	}

	public Set<Team> getTesterTeams() {
		return TesterTeams;
	}

	public void setTesterTeams(Set<Team> testerTeams) {
		TesterTeams = testerTeams;
	}

	public Set<Project> getTesterProjects() {
		return testerProjects;
	}

	public void setTesterProjects(Set<Project> testerProjects) {
		this.testerProjects = testerProjects;
	}
	
	
}
