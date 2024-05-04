package com.dance.mo.Entities;

import com.dance.mo.Entities.Enumarations.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long TicketId;
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;
    private double price;
    private String availability;

    @ManyToOne(cascade = CascadeType.ALL)
    private Competition competition;

    @OneToOne
    private Payment payment;

    @OneToOne
    private User buyer;
}
