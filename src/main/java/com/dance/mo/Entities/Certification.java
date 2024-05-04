package com.dance.mo.Entities;

import jakarta.persistence.*;

@Entity
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isCertified;

    @OneToOne
    private DanceSchool danceSchool;

    public Certification() {
    }

    public Certification(String name, boolean isCertified, DanceSchool danceSchool) {
        this.name = name;
        this.isCertified = isCertified;
        this.danceSchool = danceSchool;
    }

    // Getters and Setters
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

    public boolean isCertified() {
        return isCertified;
    }

    public void setCertified(boolean certified) {
        isCertified = certified;
    }

    public DanceSchool getDanceSchool() {
        return danceSchool;
    }

    public void setDanceSchool(DanceSchool danceSchool) {
        this.danceSchool = danceSchool;
    }
}

