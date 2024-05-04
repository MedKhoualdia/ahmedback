package com.dance.mo.Entities;

import com.dance.mo.Entities.Enumarations.FeedbackType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long feedbackId;
    private String Content;
    private Integer rating;
    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @ManyToOne
    private Competition competition;

    @ManyToOne
    private DanceSchool danceSchool;
    ///

}
