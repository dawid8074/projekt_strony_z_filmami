package com.example.demo.Controller;

import com.example.demo.Entity.Wideo;
import com.example.demo.Service.WideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/wideos")
@AllArgsConstructor
public class WideoController {

    private WideoService wideoService;
    @GetMapping("/Szukane={szukane}")
    public Page<Object> getVideoC(@PathVariable String szukane) {
        return wideoService.dajWszystkie(1,szukane);
    }
    @GetMapping("/page={page}/Szukane={szukane}")
    public Page<Object> getVideo2C( @PathVariable int page,@PathVariable String szukane ) {
        return wideoService.dajWszystkie(page,szukane);
    }

//        Optional<Wideo> video = wideoService.getVideoById(id);
//        if (video.isPresent()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType("video/mp4"))
//                    .body(video.get().getWideo());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//        return


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getVideoC(@PathVariable UUID id) {
        Optional<Wideo> video = wideoService.getVideoById(id);
        if (video.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(video.get().getWideo());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/miniatura/{id}")
    public ResponseEntity<byte[]> geMiniaturaC(@PathVariable UUID id) {
        Optional<Wideo> video = wideoService.getVideoById(id);
        if (video.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/jpeg"))
                    .body(video.get().getMiniatura());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/NameAndOpisAndIdAndNameAndDate/Id={id}")
    public Object getNameAndOpisAndIdAndNameAndDate(@PathVariable UUID id){
        return wideoService.dajNameAndOpisAndIdAndNameAndDate(id);
    }
    @GetMapping("/Propozycje/{id}")
    public List<Object> dajPropozycjeC(@PathVariable UUID id){
        return wideoService.dajPropozycje(id);
    }

    @PutMapping("/name={name}/opis={opis}/id_autora={id_autora}")
    public UUID addVideoC(@RequestParam("file") MultipartFile file,@PathVariable String name,@PathVariable String opis, @PathVariable UUID id_autora) {
        return  wideoService.dodajWideoS(file, name, opis, id_autora);
    }
    @PostMapping("/name={newName}/id={id}")
    public UUID editVideoNameC(@PathVariable String newName,@PathVariable UUID id ) throws Exception {
        return wideoService.edytujWIdeoNameS(newName, id);
    }
    @PostMapping("/opis={newOpis}/id={id}")
    public UUID editVideoOpisC(@PathVariable String newOpis,@PathVariable UUID id ) throws Exception {
        return wideoService.edytujWideoOpisS(newOpis, id);
    }

    @DeleteMapping("/videos/{id}")
    public ResponseEntity deleteWideoC(@PathVariable UUID id) {
        if (wideoService.deleteWideoS(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
