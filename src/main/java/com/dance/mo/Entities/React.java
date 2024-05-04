package com.dance.mo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class React implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactId;
    private boolean liked;
    private boolean dislike;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private ForumPost forumPost;
    @ManyToOne(cascade = CascadeType.ALL)
    User user;



}