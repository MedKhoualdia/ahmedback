package com.dance.mo.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Criterion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String technique;
    private String artistry;
    private String choreography;
    private String performanceQuality;
    private String stagePresence;
    private String expressionAndEmotion;
    private String synchroAndPrecision;

    @OneToMany(mappedBy = "criterion")
    private Set<Score> scores;
}
