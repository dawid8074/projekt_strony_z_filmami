package com.example.demo;

import com.example.demo.Repository.UzytkownikRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UzytkownikDetailServiceImpl implements UserDetailsService {

    private UzytkownikRepository uzytkownikRepository;

    public UzytkownikDetailServiceImpl(UzytkownikRepository uzytkownikRepository) {
        this.uzytkownikRepository = uzytkownikRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return uzytkownikRepository.findByUserName(username).orElseThrow(() ->new RuntimeException("nie znaleziono uzytkownika w bazie"));
    }




}
