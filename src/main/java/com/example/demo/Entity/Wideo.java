package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Wideo {
    @Id
    private UUID id;

    @ManyToOne
    private Uzytkownik uzytkownik;

    private String name;

    private String opis;

    private byte[] wideo;

    private byte[] miniatura;

    private LocalDateTime date;

    private int like;

    private int dislike;

    private String status;

    @OneToMany(orphanRemoval = true,mappedBy = "wideo")
    @JsonBackReference
    private Set<Opinion> opinions;

//    public Wideo( byte[] wideo, String name) {
//        this.wideo = wideo;
//        id=UUID.randomUUID();
//        date= LocalDateTime.now();
//        like=0;
//        dislike=0;
//        status="dodane";
//        this.name=name;
//    }
}
