package com.example.demo.Controller;

import com.example.demo.Entity.OpinionLike;
import com.example.demo.EntityDTO.OpinionDTO.OpinionEditTekstDTO;
import com.example.demo.EntityDTO.OpinionDTO.OpinionNewDTO;
import com.example.demo.Service.OpinionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Opinion")
@AllArgsConstructor
//to chyba dla reaquestow z fronta
@CrossOrigin
public class OpinionController {

    private OpinionService opinionService;

    @GetMapping("/dajOpinieDlaID/{id}")
    public String dajTekstOpiniiC(@PathVariable UUID id){
        return opinionService.dajTesktOpiniiS(id);
    }

    @GetMapping("/dajOpinie/id={id_wideo}")
    public List<Object> dajOpinieDlaID(@PathVariable UUID id_wideo)
    {
        return opinionService.dajOpinieDlaIdS(id_wideo);
    }
    @GetMapping("/dajZgloszone")
    public List<Object> dajZgloszoneC(){
        return opinionService.dajZgloszoneS();
    }
    @PostMapping("/akceptuj_komentarz/id={id_opinii}")
    public void akceptuj(@PathVariable UUID id_opinii)
    {
        opinionService.akceptujOpinieS(id_opinii);
    }
    @PostMapping("/dodajOpinie")
    public UUID dodajOpinieC(@RequestBody OpinionNewDTO opinionNewDTO)
    {
        return opinionService.dodajOpinieS(opinionNewDTO);
    }

    @PutMapping("/edytujTekstOpinii")
    public UUID edytujTekstOpinniC(@RequestBody OpinionEditTekstDTO opinionEditTekstDTO)
    {
        return opinionService.edytujTekstOpinniS(opinionEditTekstDTO);
    }

    @DeleteMapping("/UsunOpinie/id={id}")
    public void usunOpinieC(@PathVariable UUID id)
    {
        opinionService.usunOpinieS(id);
    }

    @GetMapping("/dajOpinieLike/id_opinion={id_opinii}/id_user={id_user}")
    public ResponseEntity<OpinionLike> dajLikeAndDislikeC(@PathVariable UUID id_opinii, @PathVariable UUID id_user)
    {
        Optional<OpinionLike> like = opinionService.dajLikeAndDislikeS(id_opinii, id_user);
        if (like.isPresent()) {
            return new ResponseEntity<>(like.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping("/dodajlikeOpinni/opinion={id_opinion}/user={id_user}")
    public UUID dajLikeC(@PathVariable UUID id_opinion,@PathVariable UUID id_user)
    {
        return opinionService.dajLikeOpinionS(id_opinion, id_user);
    }

    @PostMapping("/dodajdislikeOpinni/opinion={id_opinion}/user={id_user}")
    public UUID dajdislikeC(@PathVariable UUID id_opinion, @PathVariable UUID id_user)
    {
        return opinionService.dajDislikeOpinionS(id_opinion, id_user);
    }

    @PostMapping("/zabierzLikeOpinni/opinion={id_opinion}/user={id_user}")
    public void zabierzLikeC(@PathVariable UUID id_opinion, @PathVariable UUID id_user)
    {
        opinionService.zabierzLikeS(id_opinion, id_user);
    }

    @PostMapping("/zabierzDislikeOpinni/opinion={id_opinion}/user={id_user}")
    public void zabierzDislikeC(@PathVariable UUID id_opinion, @PathVariable UUID id_user)
    {

        opinionService.zabierzDislikeS(id_opinion, id_user);
    }

    @PostMapping("/zglosOpinie/id={id_opinii}")
    public void zglosOpinieC(@PathVariable UUID id_opinii)
    {
        opinionService.zglosOpinieS(id_opinii);
    }
}
