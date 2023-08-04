package com.example.demo.EntityDTO.OpinionDTO;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OpinionNewDTO {
    private UUID id_uzytkownik;
    private UUID id_wideo;
    private String tekst;
}
