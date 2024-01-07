package com.tracker.app.entities;

import java.util.Set;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value="admin")
public class Admin extends Employe {
	
	public Admin() {}
	
	public Admin(String name, String email,Set<Bug> bugs, Set<Comment> comments,
			Set<Team> teamsManaged, Set<Project> projectsManaged,Bug bug) {
		super(name,email, bugs, comments, bug);
	}
	
	public Admin(String name,String email) {
		super(name,email);
	}
}