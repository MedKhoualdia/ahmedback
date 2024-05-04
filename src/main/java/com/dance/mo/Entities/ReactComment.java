package com.dance.mo.Entities;

import com.dance.mo.Entities.Enumarations.ReactType;
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
public class ReactComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactsId;
    private LocalDate dateReact;

    @Enumerated(EnumType.STRING)
    private ReactType reactType;

    @ManyToOne
    private Comment comment;



}
