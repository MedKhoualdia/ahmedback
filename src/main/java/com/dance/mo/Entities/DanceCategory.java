package com.dance.mo.Entities;
import jakarta.persistence.*;

import java.util.List;





@Entity
public class DanceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "danceCategory", cascade = CascadeType.ALL)
    private List<DanceStyle> danceStyles;

    @ManyToOne
    private Competition competition;






}
