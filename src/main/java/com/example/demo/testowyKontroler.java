package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class testowyKontroler {


    //@RolesAllowed("USER")
    @GetMapping("/test")
    @ResponseBody
    public String testfunkcja(){
        return "czesc o to ja. a to moj security";
    }
    @GetMapping("/test2")
    @ResponseBody
    public String testfunkcja2(){
        return "czesc o to ja. a to moj security numer 2 !!!!!!!!!!!!!!!!!!!!!1";
    }
    @GetMapping("/test3")
    @ResponseBody
    public String testfunkcja3(){
        return "po zlogowaniu";
    }

    @GetMapping("/login")
    public String login(){

        return "login";
    }

    @GetMapping("/testowa")
    public String testowa(Principal principal, Model model){
        if (Optional.ofNullable(principal).isPresent()) {
            model.addAttribute("nameBadany", principal.getName());
            model.addAttribute("testowa_zmienna",principal.getName());
        }
        return "testowa";
    }

//    @GetMapping("/logout")
//    public String logout(){
//        return "logout";
//    }
}
