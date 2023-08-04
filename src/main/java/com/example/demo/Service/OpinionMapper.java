package com.example.demo.Service;

import com.example.demo.Entity.Opinion;
import com.example.demo.Entity.Status;
import com.example.demo.EntityDTO.OpinionDTO.OpinionNewDTO;
import com.example.demo.Repository.UzytkownikRepository;
import com.example.demo.Repository.WideoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OpinionMapper {
    private final UzytkownikRepository uzytkownikRepository;
    private final WideoRepository wideoRepository;

    public Opinion mapInputtoEntity(OpinionNewDTO opinionNewDTO)
    {
        return Opinion.builder()
                .id(UUID.randomUUID())
                .uzytkownik(uzytkownikRepository.findById(opinionNewDTO.getId_uzytkownik())
                        .orElseThrow(()->new EntityNotFoundException("Nie znalezono uzytkownika")))
                .wideo(wideoRepository.findById(opinionNewDTO.getId_wideo())
                        .orElseThrow(()->new EntityNotFoundException("Nie znaleziono wideo")))
                .tekst(opinionNewDTO.getTekst())
                .dislike(0)
                .like(0)
                .suma(0)
                .status(Status.dodano)
                .date(LocalDateTime.now())
                .build();
    }

}
