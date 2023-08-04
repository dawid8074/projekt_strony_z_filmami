package com.example.demo.Controller;

import com.example.demo.Entity.Uzytkownik;
import com.example.demo.EntityDTO.UzytkownikInputDTO;
import com.example.demo.Service.UzytkownikService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/User")
@AllArgsConstructor
//to chyba dla reaquestow z fronta
@CrossOrigin
public class UzytkownikController {
    private UzytkownikService uzytkownikService;

    //@RolesAllowed("ADMIN")
    @GetMapping("/dajWszystkich")
    public List<Uzytkownik> wszystkieUzytkowniki(){
        return uzytkownikService.wyszukajUzytkownikow();
    }

    @GetMapping("/dajModerow")
    public List<Uzytkownik> WszyscyModerzy(){
        return uzytkownikService.wyszukajModerow();
    }

    @GetMapping( "/daj/{id}")
    public String dajRoleC(@PathVariable UUID id){
        return uzytkownikService.dajrole(id);
    }

    @PostMapping("/dodaj")
    public Uzytkownik dodajUzytkownik(@RequestBody UzytkownikInputDTO uzytkownikInputDTO) throws Exception {
        return uzytkownikService.dodajUzytkownika(uzytkownikInputDTO);

    }

    @DeleteMapping("/del/{id}")
    public void usunUzytkownik(@PathVariable UUID id){
        uzytkownikService.usunUzytkownika(id);
    }


    @DeleteMapping("del/all")
    public void usunWszystkich()
    {
        uzytkownikService.UsunWszystkich();
    }
    @PutMapping("/edytujRole/user={id}/rola={rola}")
    public UUID edytujRoleC(@PathVariable UUID id, String rola){
        return uzytkownikService.edytujRole(id, rola);
    }
    //@PutMapping-edycja



}
