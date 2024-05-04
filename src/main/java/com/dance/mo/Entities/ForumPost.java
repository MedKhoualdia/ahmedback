package com.dance.mo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForumPost implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;

    private String postContent;
    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;


    private float postRatingScore;
    private String imageUrl;


    @OneToMany( mappedBy="forumPost")
    private Set<Comment> comments;


    @ManyToOne(cascade = CascadeType.ALL )
    @JoinColumn(name ="postCreator ")
    private User postCreator;

    @OneToMany(cascade = CascadeType.ALL,mappedBy ="forumPost" )
    List<React>reacts;

}