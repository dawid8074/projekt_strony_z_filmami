package com.example.demo.Repository;

import com.example.demo.Entity.Opinion;
import com.example.demo.Entity.OpinionLike;
import com.example.demo.Entity.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OpinionLikeRpository extends JpaRepository<OpinionLike, UUID> {

    Optional<OpinionLike> findByUzytkownikIdAndOpinionId(UUID id_uzytownik,UUID id_opinion);
    //findAllByUzytkownik_IdAndOpinion_Id(UUID id_uzytownik,UUID id_opinion);
    List<OpinionLike> findByOpinion_Id(UUID id_opinion);
//    @Query("Select o.like, o.dislike from Opinion o where o.id=:id")
//    Optional<Object> findLikeAndDislike(UUID id_opinion, UUID id_user);

    Optional<OpinionLike> findAllByUzytkownikAndAndOpinion(Uzytkownik uzytkownik, Opinion opinion);
}
