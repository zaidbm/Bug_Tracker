package com.tracker.app.entities;

import java.util.HashSet;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_team")
    private Long id;

    @NotBlank(message = "Team name is required")
    @Size(max = 30, message = "Team name cannot exceed 30 characters")
    @Column(name = "nom")
    private String name;

    @OneToOne(mappedBy = "team")
    private Project project;

    @ManyToMany(targetEntity = Manager.class)
    @JoinTable(
        name = "Manager_teams",
        joinColumns = @JoinColumn(name = "id_team"),
        inverseJoinColumns = @JoinColumn(name = "id_manager")
    )
    private Set<Manager> teamsManagers = new HashSet<Manager>();

    @ManyToMany()
    @JoinTable(
        name = "developer_teams",
        joinColumns = @JoinColumn(name = "id_team"),
        inverseJoinColumns = @JoinColumn(name = "id_developer")
    )
    private Set<Developer> teamsMembers = new HashSet<Developer>();

    @ManyToMany()
    @JoinTable(
        name = "tester_teams",
        joinColumns = @JoinColumn(name = "id_team"),
        inverseJoinColumns = @JoinColumn(name = "id_tester")
    )
    private Set<Tester> teamsTesters = new HashSet<Tester>();
    @Transient
    private Set<Employe> allTeamMembers = new HashSet<>();

    public Team() {
    }

    public Team(String name) {
    	this.name=name;
    }

    public Team(String name, Project project, Set<Manager> teamsManagers, Set<Developer> teamsMembers,
            Set<Tester> teamsTesters) {
        this.name = name;
        this.project = project;
        this.teamsManagers = teamsManagers;
        this.teamsMembers = teamsMembers;
        this.teamsTesters = teamsTesters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Manager> getTeamsManagers() {
        return teamsManagers;
    }

    public void setTeamsManagers(Set<Manager> teamsManagers) {
        this.teamsManagers = teamsManagers;
    }

    public Set<Developer> getTeamsMembers() {
        return teamsMembers;
    }

    public void setTeamsMembers(Set<Developer> teamsMembers) {
        this.teamsMembers = teamsMembers;
    }

    public Set<Tester> getTeamsTesters() {
        return teamsTesters;
    }

    public void setTeamsTesters(Set<Tester> teamsTesters) {
        this.teamsTesters = teamsTesters;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public Set<Employe> getAllMembers(){
    	allTeamMembers.addAll(teamsManagers);
        allTeamMembers.addAll(teamsMembers);
        allTeamMembers.addAll(teamsTesters);
		return allTeamMembers;
    }
}
