package com.tracker.app.entities;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long id;

    @NotBlank(message = "Category is required")
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Bug> bugs;
	
	public Category() {}
	
	public Category(String name) {
		this.name = name;
	}
	public Category(String name, Set<Bug> bugs) {
		this.name = name;
		this.bugs = bugs;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<Bug> getBugs() {
		return bugs;
	}
	public void setBugs(Set<Bug> bugs) {
		this.bugs = bugs;
	}
	public String getName() {
		return name;
	}
	public void setName(String category) {
		this.name = category;
	}
	//set all occurences of category in bug table to null 
	@PreRemove
	public void setIdCategoryInBugToNull() {
		for (Bug bug : bugs) {
			bug.setCategory(null);
		}
	}
}
