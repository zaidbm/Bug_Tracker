package com.tracker.app.entities;

import java.util.Date;
import java.util.Set;


import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Bug {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bug")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    //@Size(max = 1000, message = "")
    @Column(columnDefinition = "TEXT")
    private String stepsToReproduce;

    @Column(columnDefinition = "TEXT")
    private String expectedResult;

    @Column(columnDefinition = "TEXT")
    private String actualResult;

    @Size(max = 50, message = "Git branch cannot exceed 50 characters")
    private String gitBranch;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @NotNull(message = "progress is required")
    private String progress;

    @NotNull(message = "status is required")
    private String status;//solved/unsolved

    @NotNull(message = "platform is required")
    private String platform;

    @NotNull(message = "source is required")
    private String source;

    private String report;

    @NotNull(message = "Category is required")
    @ManyToOne
    @JoinColumn(name = "id_cat")
    private Category category;

    @NotNull(message = "Project is required")
    @ManyToOne
    @JoinColumn(name = "id_project")
    private Project project;

    @OneToOne
    @JoinColumn(name = "bugCreator")
    private Employe bugCreator;
    
    @NotNull(message = "Employee is required")
    @ManyToOne
    @JoinColumn(name = "assignedTo")
    private Employe assignedTo;
    
    
    
    @OneToMany(mappedBy = "bug", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "bug", fetch = FetchType.LAZY)
    private Set<Attachment> attachments;

    @NotNull(message = "Priority is required")
    @ManyToOne
    @JoinColumn(name = "id_priority")
    private Priority priority;

    @NotNull(message = "Severity is required")
    @ManyToOne
    @JoinColumn(name = "id_severity")
    private Severity severity;

    @ManyToMany(targetEntity = Tag.class)
    @JoinTable(
            name = "tags_bugs",
            joinColumns = @JoinColumn(name = "id_bug"),
            inverseJoinColumns = @JoinColumn(name = "id_tag"))
    Set<Tag> tagsPerBug;
    
    
    
    @OneToMany(mappedBy = "bug", fetch = FetchType.LAZY)
    private Set<Notification> notifications;
	public Bug() {}
	
	public Bug(String title, String description, String stepsToReproduce, String expectedResult, String actualResult,
			String gitBranch, Date createdAt, String progress, String status, String platform,
			String source, String report, Category category, Project project, Employe assignedTo,
			Set<Comment> comments, Set<Attachment> attachments, Priority priority, Severity severity,
			Set<Tag> tagsPerBug,Employe bugCreator) {
		this.title = title;
		this.description = description;
		this.stepsToReproduce = stepsToReproduce;
		this.expectedResult = expectedResult;
		this.actualResult = actualResult;
		this.gitBranch = gitBranch;
		this.createdAt = createdAt;
		this.progress = progress;
		this.status = status;
		this.platform = platform;
		this.source = source;
		this.report = report;
		this.category = category;
		this.project = project;
		this.assignedTo = assignedTo;
		this.bugCreator=bugCreator;
		this.comments = comments;
		this.attachments = attachments;
		this.priority = priority;
		this.severity = severity;
		this.tagsPerBug = tagsPerBug;
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
	public String getStepsToReproduce() {
		return stepsToReproduce;
	}
	public void setStepsToReproduce(String stepsToReproduce) {
		this.stepsToReproduce = stepsToReproduce;
	}
	public String getExpectedResult() {
		return expectedResult;
	}
	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}
	public String getActualResult() {
		return actualResult;
	}
	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}
	public String getGitBranch() {
		return gitBranch;
	}
	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
	
	public Employe getEmployeAssigned() {
		return assignedTo;
	}

	public void setEmployeAssigned(Employe employeAssigned) {
		this.assignedTo = employeAssigned;
	}

	public Employe getBugCreator() {
		return bugCreator;
	}

	public void setBugCreator(Employe bugCreator) {
		this.bugCreator = bugCreator;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Attachment> getAttachmnts() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Set<Tag> getTagsPerBug() {
		return tagsPerBug;
	}

	public void setTagsPerBug(Set<Tag> tagsPerBug) {
		this.tagsPerBug = tagsPerBug;
	}

	public String toString() {
		return this.title;
	}

	public Employe getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Employe assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	
	
	
}
