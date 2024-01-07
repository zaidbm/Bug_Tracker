package com.tracker.app.entities;

import java.util.Date;
import java.util.Set;
import jakarta.persistence.Column;
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
import jakarta.persistence.PreRemove;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class Project {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    private String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private String status;

    private String client;

    @NotBlank(message = "GitHub repository is required")
    private String githubRepo;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Bug> bugs;

    @OneToOne
    @JoinColumn(name = "id_team")
    private Team team;

    @ManyToMany(targetEntity = Manager.class)
	@JoinTable(
			  name = "manager_projects", 
			  joinColumns = @JoinColumn(name = "id_Project"), 
			  inverseJoinColumns = @JoinColumn(name = "id_manager"))
	private Set<Manager> projectManagers;
	
	public Project() {}
	
	public Project(String title, String description, String version, Date dateDebut, Date dateFin, String status,
			String client, Set<Bug> bugs, Team team, Manager manager,String githubRepo) {
		this.title = title;
		this.description = description;
		this.version = version;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.status = status;
		this.client = client;
		this.bugs = bugs;
		this.team = team;
		this.githubRepo=githubRepo;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Set<Bug> getBugs() {
		return bugs;
	}

	public void setBugs(Set<Bug> bugs) {
		this.bugs = bugs;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getGithubRepo() {
		return githubRepo;
	}

	public void setGithubRepo(String githubRepo) {
		this.githubRepo = githubRepo;
	}
	
	public Set<Manager> getProjectManagers() {
		return projectManagers;
	}

	public void setProjectManagers(Set<Manager> projectManagers) {
		this.projectManagers = projectManagers;
	}

	@Override
	public String toString() {
		return title;
	}

	//set all occurences of project in bug table to null 
	@PreRemove
	public void setIdProjectInBugToNull() {
		for (Bug bug : bugs) {
			bug.setProject(null);
		}
	}
}
