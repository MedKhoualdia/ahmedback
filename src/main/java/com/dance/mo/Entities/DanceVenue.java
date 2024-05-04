package com.dance.mo.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DanceVenue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long danceVenueId;
    private String name;
    private int numberOfSeat;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Ticket> tickets;

    @OneToOne(mappedBy = "danceVenue")
    private Competition competition;
}
