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
public class Severity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_severity")
    private Long id;

    @NotBlank(message = "Severity name is required")
    @Size(max = 255, message = "Severity name must be at most 255 characters")
    private String name;

    @OneToMany(mappedBy = "severity")
    private Set<Bug> bugs;

    public Severity() {
    }
    public Severity(String severity) {
        this.name = severity;
        
    }
    public Severity(String severity, Set<Bug> bugs) {
        this.name = severity;
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

    public void setName(String severity) {
        this.name = severity;
    }

    public Set<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(Set<Bug> bugs) {
        this.bugs = bugs;
    }

    @PreRemove
    public void setIdSeverityInBugToNull() {
        for (Bug bug : bugs) {
            bug.setSeverity(null);
        }
    }
}
