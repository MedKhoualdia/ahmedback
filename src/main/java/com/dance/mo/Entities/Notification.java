package com.dance.mo.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reciever_id")
    private User receiver;
    private String status;
    private String message;
    private Date sendDate;
    private Long reclamationId;
    private Boolean seen;
}
