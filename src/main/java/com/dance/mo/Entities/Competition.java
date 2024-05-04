package com.dance.mo.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Competition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long CompetitionId;
    private String Name;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String Rules;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "competition")
    private Set<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private User competitor;

    @OneToOne
    private DanceVenue danceVenue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competition")
    private Set<Multimedia> multimedia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competition")
    private List<DanceCategory> danceCategories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competition")
    private List<Feedback> feedback;
}
