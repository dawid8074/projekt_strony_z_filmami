package com.example.demo.Repository;

import com.example.demo.Entity.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UzytkownikRepository extends JpaRepository<Uzytkownik, UUID> {

    Optional<Uzytkownik> findByUserName(String username);
    List<Uzytkownik> findByRoleNot(String role);

    List<Uzytkownik> findByRole(String role);
    boolean existsByEmailAddress(String y);

    boolean existsUzytkownikByUserName(String x);

}
