package com.dance.mo.Entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL )
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate reclamationDate;

    private String status;
    private String result;

    private String description;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private  byte[] screenshot;


}

