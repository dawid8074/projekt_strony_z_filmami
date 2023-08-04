package com.example.demo.Service;

import com.example.demo.Entity.Opinion;
import com.example.demo.Entity.OpinionLike;
import com.example.demo.Entity.Status;
import com.example.demo.EntityDTO.OpinionDTO.OpinionEditTekstDTO;
import com.example.demo.EntityDTO.OpinionDTO.OpinionNewDTO;
import com.example.demo.Repository.OpinionLikeRpository;
import com.example.demo.Repository.OpinionRepository;
import com.example.demo.Repository.UzytkownikRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class OpinionService {

    private OpinionRepository opinionRepository;
    private OpinionLikeRpository opinionLikeRpository;
    private UzytkownikRepository uzytkownikRepository;
    private OpinionMapper opinionMapper;


    //nowa opinia

    public UUID dodajOpinieS(OpinionNewDTO opinionNewDTO) {

        Opinion opinion=opinionMapper.mapInputtoEntity(opinionNewDTO);
        opinionRepository.save(opinion);
        return opinion.getId();
    }
    public String dajTesktOpiniiS(UUID id) {
        Opinion opinion=opinionRepository.getReferenceById(id);
        return opinion.getTekst();
    }

    public UUID edytujTekstOpinniS(OpinionEditTekstDTO opinionEditTekstDTO) {
        Opinion opinion = opinionRepository.getReferenceById(opinionEditTekstDTO.getId_opinion());
        opinion.setTekst(opinionEditTekstDTO.getTekst());
        opinion.setStatus(Status.dodano);
        opinionRepository.save(opinion);

        return opinion.getId();
    }
    public List<Object> dajOpinieDlaIdS(UUID id_wideo) {
        return opinionRepository.findAllAndName(Sort.by(Sort.Direction.DESC, "suma"),id_wideo);
        //return opinionRepository.findAll(Sort.by(Sort.Direction.DESC, "suma"));
    }

    public List<Object> dajZgloszoneS() {
        return opinionRepository.findAllZgloszone();
    }

    public void usunOpinieS(UUID id_opinion) {
        List<OpinionLike> opinionLike=opinionLikeRpository.findByOpinion_Id(id_opinion);

//        for (int i=0; i<opinionLike.size(); i++)
//        {
//            opinionLikeRpository.delete(opinionLike.get(i));
//        }

        Opinion opinion=opinionRepository.getReferenceById(id_opinion);
        opinionRepository.delete(opinion);

    }






    public UUID dajLikeOpinionS(UUID id_opinion, UUID id_user) {
        if (opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user, id_opinion).isEmpty()) {
            OpinionLike opinionLike= StworzOpinieLike(id_user,id_opinion, true,false);
            opinionLikeRpository.save(opinionLike);
            EdytujIloscLikeWOpinie(id_opinion, "dodaj", "like");
            return opinionLike.getId();
        }
        else {
            OpinionLike opinionLike= opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user, id_opinion).orElseThrow();
            if (!opinionLike.isLike()){
                opinionLike.setLike(true);
                EdytujIloscLikeWOpinie(id_opinion, "dodaj", "like");
                if (opinionLike.isDislike()){
                    EdytujIloscLikeWOpinie(id_opinion, "odejmij", "dislike");
                    opinionLike.setDislike(false);
                }
            }
            opinionLikeRpository.save(opinionLike);
            return opinionLike.getId();
        }
    }

    public UUID dajDislikeOpinionS(UUID id_opinion, UUID id_user) {
        if (opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user, id_opinion).isEmpty()) {
            OpinionLike opinionLike= StworzOpinieLike(id_user,id_opinion, false,true);
            opinionLikeRpository.save(opinionLike);
            EdytujIloscLikeWOpinie(id_opinion, "dodaj", "dislike");
            return opinionLike.getId();
        }
        else {
            OpinionLike opinionLike= opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user,id_opinion).orElseThrow();
            if (!opinionLike.isDislike()){
                opinionLike.setDislike(true);
                EdytujIloscLikeWOpinie(id_opinion, "dodaj", "dislike");
                if (opinionLike.isLike()){
                    EdytujIloscLikeWOpinie(id_opinion, "odejmij", "like");
                    opinionLike.setLike(false);
                }
            }
            opinionLikeRpository.save(opinionLike);
            return opinionLike.getId();
        }

    }

    public void zabierzLikeS(UUID id_opinion, UUID id_user) {
        OpinionLike opinionLike= opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user, id_opinion)
                .orElseThrow(()->new RuntimeException("Próba zabrania like gdy go nie ma"));
        if (opinionLike.isLike()){
            opinionLike.setLike(false);
            opinionLikeRpository.save(opinionLike);
        }
        EdytujIloscLikeWOpinie(id_opinion, "odejmij", "like");

    }

    public void zabierzDislikeS(UUID id_opinion, UUID id_user) {
        OpinionLike opinionLike= opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user, id_opinion)
                .orElseThrow(()->new RuntimeException("Próba zabrania dislike gdy go nie ma"));
        if (opinionLike.isDislike()){
            opinionLike.setDislike(false);
            opinionLikeRpository.save(opinionLike);
        }
        EdytujIloscLikeWOpinie(id_opinion, "odejmij", "dislike");
    }


    OpinionLike StworzOpinieLike(UUID id_user, UUID id_opinni, boolean like, boolean dislike){
        return OpinionLike.builder()
                .id(UUID.randomUUID())
                .uzytkownik(uzytkownikRepository.findById(id_user).orElseThrow(()->new
                        EntityNotFoundException("Nie znaleziono użytkownika przy dawaniu like")))
                .opinion(opinionRepository.findById(id_opinni).orElseThrow(()->new
                        EntityNotFoundException("Nie znaleziono opinii do dania like")))
                .like(like)
                .dislike(dislike)
                .build();

    }

    void EdytujIloscLikeWOpinie(UUID id_opinion, String operacja, String na_czym)
    {
        Opinion opinion=opinionRepository.findById(id_opinion).orElseThrow();

        switch(operacja){

            case "dodaj":
                switch (na_czym) {
                    case "like" -> {
                        opinion.setLike(opinion.getLike() + 1);
                        opinion.setSuma(opinion.getSuma()+1);
                    }
                    case "dislike" -> {
                        opinion.setDislike(opinion.getDislike() + 1);
                        opinion.setSuma(opinion.getSuma()-1);
                    }
                }
                break;

            case "odejmij":
                switch (na_czym) {
                    case "like" -> {
                        opinion.setLike(opinion.getLike() - 1);
                        opinion.setSuma(opinion.getSuma()-1);
                    }
                    case "dislike" -> {
                        opinion.setDislike(opinion.getDislike() - 1);
                        opinion.setSuma(opinion.getSuma()+1);
                    }
                }
                break;
        }
        opinionRepository.save(opinion);
    }


    public Optional<OpinionLike> dajLikeAndDislikeS(UUID id_opinii, UUID id_user) {
        return  opinionLikeRpository.findByUzytkownikIdAndOpinionId(id_user,id_opinii);
//        Uzytkownik uzytkownik=uzytkownikRepository.getReferenceById(id_user);
//        Opinion opinion=opinionRepository.getReferenceById(id_opinii);
//        return opinionLikeRpository.findAllByUzytkownikAndAndOpinion(uzytkownik,opinion);
    }

    public void zglosOpinieS(UUID id_opinii) {
        Opinion opinion=opinionRepository.getReferenceById(id_opinii);
        opinion.setStatus(Status.zgloszono);
        opinionRepository.save(opinion);
    }


    public void akceptujOpinieS(UUID id_opinii) {
        Opinion opinion=opinionRepository.getReferenceById(id_opinii);
        opinion.setStatus(Status.zweryfikowano);
        opinionRepository.save(opinion);
    }
}
