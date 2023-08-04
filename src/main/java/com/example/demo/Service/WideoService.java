package com.example.demo.Service;

import com.example.demo.Entity.Uzytkownik;
import com.example.demo.Entity.Wideo;
import com.example.demo.Repository.UzytkownikRepository;
import com.example.demo.Repository.WideoRepository;
import lombok.AllArgsConstructor;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WideoService {
    private WideoRepository wideoRepository;
    private UzytkownikRepository uzytkownikRepository;
    public Optional<Wideo> getVideoById(UUID id) {
        return wideoRepository.findById(id);
    }
    public UUID dodajWideoS(MultipartFile file, String name, String opis, UUID id_autora){
        Wideo wideo = new Wideo();
        try {
            wideo.setWideo(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("nie udało się dodać wideo");
        }
        wideo.setName(name);
        wideo.setOpis(opis);
        wideo.setId(UUID.randomUUID());
        wideo.setStatus("dodano");
        Uzytkownik uzytkownik=uzytkownikRepository.getReferenceById(id_autora);
        wideo.setUzytkownik(uzytkownik);
        wideo.setDate(LocalDateTime.now());

        byte[] miniatura;
        try (InputStream videoStream = file.getInputStream()) {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoStream);
            frameGrabber.start();
            Frame frame = frameGrabber.grabImage();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bufferedImage = converter.getBufferedImage(frame);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            frameGrabber.stop();
            miniatura=baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Problem przy tworzeniu miniatury", e);
        }
        wideo.setId(UUID.randomUUID());
        wideo.setMiniatura(miniatura);
        wideoRepository.save(wideo);
        return wideo.getId();
    }

    public boolean deleteWideoS(UUID id) {
        Optional<Wideo> wideo = wideoRepository.findById(id);
        if (wideo.isPresent()) {
            wideoRepository.delete(wideo.get());
            return true;
        } else {
            return false;
        }
    }

    public UUID edytujWIdeoNameS(String newName, UUID id) throws Exception {
        Optional<Wideo> wideo = wideoRepository.findById(id);
        if (wideo.isPresent()) {
            wideo.get().setName(newName);
            wideoRepository.save(wideo.get());
            return wideo.get().getId();
        }
        else {
            throw new Exception("Nie istnieje takie wideo");
        }
    }

    public UUID edytujWideoOpisS(String newOpis, UUID id) throws Exception {
        Optional<Wideo> wideo = wideoRepository.findById(id);
        if (wideo.isPresent()) {
            wideo.get().setOpis(newOpis);
            wideoRepository.save(wideo.get());
            return wideo.get().getId();
        }
        else {
            throw new Exception("Nie istnieje takie wideo");
        }
    }

    public Page<Object> dajWszystkie(int page, String szukane) {
        Pageable PageWithTwoElements = PageRequest.of((page-1),2);
        Page<Object> allProducts;
        if (szukane.equals("")){
            allProducts = wideoRepository.findall(PageWithTwoElements);
        }
        else {
            allProducts = wideoRepository.findAllByNameLike(szukane,PageWithTwoElements);
        }

        return allProducts;


    }

    public Object dajNameAndOpisAndIdAndNameAndDate(UUID id) {
        return wideoRepository.getNameAndOpisAndIdAndNameAndDate(id);
    }

    public List<Object> dajPropozycje(UUID id) {
        return wideoRepository.findRandomThree(id);
    }
    //        return wideoRepository.getWideoIDandName();
}
