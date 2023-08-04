package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OpinionLike {
    @Id
    private UUID id;

    @ManyToOne
    @JsonBackReference
    private Opinion opinion;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "uzytkownik_id")
    private Uzytkownik uzytkownik;

    private boolean like;

    private boolean dislike;
}
