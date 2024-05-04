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
@Builder
@ToString
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String content;
    private Date commentDate;


    @JsonIgnore
    @ManyToOne()
    private ForumPost forumPost;
    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<ReactComment> reactsReactComments;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<SousComment> sousComments;

    @ManyToOne( )
    private User user;


}

