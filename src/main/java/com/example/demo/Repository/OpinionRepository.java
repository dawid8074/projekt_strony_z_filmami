package com.example.demo.Repository;

import com.example.demo.Entity.Opinion;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, UUID> {

    @Query("Select o.id, o.date, o.tekst, o.like, o.dislike, o.suma, u.userName, u.id, o.status from Opinion o inner join Uzytkownik u ON u.id=o.uzytkownik left join Wideo w ON w.id=o.wideo where w.id=:id")
    List<Object> findAllAndName(Sort suma,UUID id);
    @Query("Select o.id, o.date, o.tekst, o.like, o.dislike, o.suma, u.userName from Opinion o inner join Uzytkownik u ON u.id=o.uzytkownik where o.status=Status.zgloszono")
    List<Object> findAllZgloszone();
}
