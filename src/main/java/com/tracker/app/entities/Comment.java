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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Comment {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_comment")
	private Long id;
	private String body;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@ManyToOne
	@JoinColumn(name="id_bug")
	private Bug bug;
	@OneToMany(mappedBy = "comment",fetch = FetchType.LAZY)
	private Set<Attachment> attachments;
	@ManyToOne
	@JoinColumn(name="id_employe")
	private Employe employe;
	
	public Comment() {}
	public Comment(String body, Bug bug, Set<Attachment> attachments, Employe employe) {
		super();
		this.body = body;
		this.bug = bug;
		this.attachments = attachments;
		this.employe = employe;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String comment) {
		this.body = comment;
	}
	public Bug getBug() {
		return bug;
	}
	public void setBug(Bug bug) {
		this.bug = bug;
	}
	public Set<Attachment> getAttachements() {
		return attachments;
	}
	public void setAttachements(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
	public Employe getEmploye() {
		return employe;
	}
	public void setEmploye(Employe employe) {
		this.employe = employe;
	}
	
}
