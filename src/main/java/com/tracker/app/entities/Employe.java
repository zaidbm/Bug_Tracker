package com.tracker.app.entities;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="occupation")
public class Employe {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employe")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 30, message = "Name cannot exceed 30 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Size(max = 30, message = "Name cannot exceed 30 characters")
    private String email;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY)
    private Set<Bug> bugs;

    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private Set<Comment> comments;
    
    @OneToOne(mappedBy = "employe")
    private User user;
    
    @OneToOne(mappedBy = "bugCreator")
    private Bug bug;
	
    @OneToOne(mappedBy = "createdBy")
    private Notification notification;
    //private String department;
    
	public Employe() {}
	
	public Employe(String name,String email, Set<Bug> bugs, Set<Comment> comments,Bug bug ){
		this.name = name;
		this.email=email;
		this.bugs = bugs;
		this.comments = comments;
		this.bug=bug;
	}
	public Employe(String name,String email) {
		this.name = name;
		this.email=email;
	}
	public Employe(String name,String email, Set<Bug> bugs, Set<Comment> comments,Bug bug,Notification notification ){
		this.name = name;
		this.email=email;
		this.bugs = bugs;
		this.comments = comments;
		this.bug=bug;
		this.notification=notification;
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
	public Set<Bug> getBugs() {
		return bugs;
	}
	public void setBugs(Set<Bug> bugs) {
		this.bugs = bugs;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public String toString() {
		return this.name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	public Bug getBug() {
		return bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}

	public Map<Boolean,String> isAssignedToProject(Project project) {
		Employe employee=new Employe();
		Map<Boolean, String> whoIsAssigned=new HashMap<>();
		if (employee instanceof Developer) {
			Developer developer=(Developer) employee;
			if (developer.getDeveloperProjects().contains(project)) {	
				whoIsAssigned.put(true,"developer");
				return whoIsAssigned;
			}
		}
		else if (employee instanceof Manager) {
			Manager manager=(Manager) employee;
			if (manager.getManagerProjects().contains(project)) {	
				whoIsAssigned.put(true,"manager");
				return whoIsAssigned;
			}
		}
		else if (employee instanceof Tester) {
			Tester tester=(Tester) employee;
			if (tester.getTesterProjects().contains(project)) {	
				whoIsAssigned.put(true,"tester");
				return whoIsAssigned;
			}
		}
		whoIsAssigned.put(false,"noOne");
		return whoIsAssigned;
	}
	
}
