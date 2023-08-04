package com.example.demo.Service;

import com.example.demo.Entity.Uzytkownik;
import com.example.demo.EntityDTO.UzytkownikInputDTO;
import com.example.demo.Exceptions.TakenResourceException;
import com.example.demo.Exceptions.ZajetyEmail;
import com.example.demo.Repository.UzytkownikRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UzytkownikService {

    private UzytkownikRepository uzytkownikRepository;
    PasswordEncoder passwordEncoder;

    public Uzytkownik dodajUzytkownika(UzytkownikInputDTO uzytkownikInputDTO) throws Exception {
        String result = passwordEncoder.encode(uzytkownikInputDTO.getHaslo());

        Uzytkownik uzytkownik = new Uzytkownik(UUID.randomUUID(), uzytkownikInputDTO.getLogin(), result,uzytkownikInputDTO.getEmailAdress());
        if (uzytkownikRepository.existsUzytkownikByUserName(uzytkownik.getUsername()))
        {
            throw new TakenResourceException("Ten login jest zajęty");
        }
        if (uzytkownikRepository.existsByEmailAddress(uzytkownik.getEmailAddress()))
        {
            throw new ZajetyEmail("Ten email jest zajety");
        }

        uzytkownikRepository.save(uzytkownik);

        return uzytkownik;
    }

    public List<Uzytkownik> wyszukajUzytkownikow() {
        return uzytkownikRepository.findByRoleNot("ADMIN");
    }

    public String dajrole(UUID id)
    {
        Uzytkownik uzytkownik =  uzytkownikRepository.getReferenceById(id);
        return uzytkownik.getRole();

    }
    public UUID edytujRole(UUID id, String rola) {
        Uzytkownik uzytkownik =  uzytkownikRepository.getReferenceById(id);
        uzytkownik.setRole(rola);
        uzytkownikRepository.save(uzytkownik);
        return uzytkownik.getId();
    }

    public void usunUzytkownika(UUID id) {

        Uzytkownik uzytkownik = uzytkownikRepository.findById(id).get();
        uzytkownikRepository.delete(uzytkownik);
    }

    public void UsunWszystkich() {
        uzytkownikRepository.deleteAll();
    }

    public List<Uzytkownik> wyszukajModerow() {
        return uzytkownikRepository.findByRole("MOD");
    }
}
