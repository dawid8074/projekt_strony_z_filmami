package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Opinion {
    @Id
    private UUID id;

    @JsonIgnore
    @ManyToOne
    private Uzytkownik uzytkownik;

    @JsonIgnore
    @ManyToOne
    private Wideo wideo;

    private LocalDateTime date;

    private String tekst;

    private int like;

    private int dislike;

    private int suma;

    private Status status;

    @OneToMany(orphanRemoval = true,mappedBy = "opinion")
    @JsonBackReference
    private Set<OpinionLike> opinionLikes;

}
