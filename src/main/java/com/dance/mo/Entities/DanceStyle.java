package com.dance.mo.Entities;

import jakarta.persistence.*;

@Entity
public class DanceStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "dance_category_id")
    private DanceCategory danceCategory;
    // Constructeurs
    public DanceStyle() {
    }

    public DanceStyle(String name, DanceCategory danceCategory) {
        this.name = name;
        this.danceCategory = danceCategory;
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

    public DanceCategory getDanceCategory() {
        return danceCategory;
    }

    public void setDanceCategory(DanceCategory danceCategory) {
        this.danceCategory = danceCategory;
    }

    // Ajout des getters et setters pour les nouveaux champs


}
