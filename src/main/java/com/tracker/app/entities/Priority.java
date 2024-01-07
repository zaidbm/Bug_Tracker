package com.tracker.app.entities;

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

@Entity
public class Priority {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Priority is required")
    private String name;

    @OneToMany(mappedBy = "priority")
    private Set<Bug> bugs;
	
	public Priority() {}
	public Priority(String priority) {
		this.name = priority;
	}
	public Priority(String priority, Set<Bug> bugs) {
		this.name = priority;
		this.bugs = bugs;
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
	public void setName(String priority) {
		this.name = priority;
	}
	public Set<Bug> getBugs() {
		return bugs;
	}
	public void setBugs(Set<Bug> bugs) {
		this.bugs = bugs;
	}
	//set all occurences of priority in bug table to null
	@PreRemove
	public void setIdPriorityInBugToNull() {
		for (Bug bug : bugs) {
			bug.setPriority(null);
		}
	}
}