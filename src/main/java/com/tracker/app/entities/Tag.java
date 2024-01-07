package com.tracker.app.entities;

import java.util.List;
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
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag")
    private Long id;

    @NotBlank(message = "Tag is required")
    @Size(max = 25, message = "Tag name cannot exceed 25 characters")
    private String name;

    @ManyToMany(targetEntity = Bug.class)
    @JoinTable(
        name = "tags_bugs",
        joinColumns = @JoinColumn(name = "id_tag"),
        inverseJoinColumns = @JoinColumn(name = "id_bug")
    )
    List<Bug> bugsPerTag;

    public Tag() {
    }

    public Tag(String name, List<Bug> bugsPerTag) {
        this.name = name;
        this.bugsPerTag = bugsPerTag;
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

    public void setName(String tag) {
        this.name = tag;
    }

    public List<Bug> getBugsPerTag() {
        return bugsPerTag;
    }

    public void setBugsPerTag(List<Bug> bugsPerTag) {
        this.bugsPerTag = bugsPerTag;
    }
}
