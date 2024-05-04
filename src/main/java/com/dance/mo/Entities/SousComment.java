package com.dance.mo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SousComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scommentId;
    private String content;
    private Date commentDate;
    @JsonIgnore
    @ManyToOne
    private Comment comment;
    @JsonIgnore
    @ManyToOne()
    private User user;

}