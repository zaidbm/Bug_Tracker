package com.tracker.app.entities;

import java.util.Set;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value="Support")
public class CustomerSupport extends Employe {
	public CustomerSupport() {
	}

	public CustomerSupport(String name, String email, Set<Bug> bugs, Set<Comment> comments,Bug bug) {
		super(name, email, bugs, comments, bug);
	}

	

	
}
