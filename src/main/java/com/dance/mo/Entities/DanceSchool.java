package com.dance.mo.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class DanceSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;
    private String position;
    private String horaire;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "danceSchool")
    private List<Course> courses;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;


    @ManyToMany
    private List<User> dsUsers;


}
