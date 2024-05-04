package com.dance.mo.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String instructor;
    private String schedule;
    @ManyToOne
    @JoinColumn(name = "dance_school_id") // Assuming the column name in Course table
    private DanceSchool danceSchool;


}
