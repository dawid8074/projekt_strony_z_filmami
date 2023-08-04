package com.example.demo.Repository;

import com.example.demo.Entity.Wideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WideoRepository extends JpaRepository<Wideo, UUID> {

    Optional<Wideo> findById(UUID uuid);
    @Query("Select w.id, w.name, w.date, u.userName, w.miniatura from Wideo w inner join Uzytkownik u ON u.id=w.uzytkownik")
    List<Object> getWideoIDandName();
    @Query("Select w.id, w.name, w.date, u.userName, w.miniatura from Wideo w inner join Uzytkownik u ON u.id=w.uzytkownik")
    Page<Object> findall(Pageable pageable);
    @Query("Select w.id, w.name, w.date, u.userName, w.miniatura from Wideo w inner join Uzytkownik u ON u.id=w.uzytkownik WHERE  w.name like %:name%")
    Page<Object> findAllByNameLike(String name, Pageable pageable);
    @Query("Select w.name, w.opis, u.userName, u.id, w.date from Wideo w inner join Uzytkownik u ON u.id=w.uzytkownik WHERE w.id=:idvar")
    Object getNameAndOpisAndIdAndNameAndDate(UUID idvar);

    @Query("Select w.id, w.name, w.date, w.miniatura from Wideo w WHERE w.id != :id ORDER BY RANDOM() limit 3")
    List<Object> findRandomThree(UUID id);

}
