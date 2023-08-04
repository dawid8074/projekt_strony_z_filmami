package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Uzytkownik implements UserDetails {
    @Id
    private UUID id;
    @Column(unique = true)
    private String userName;
    private String haslo;
    private String role;

    @Column(unique = true)
    private String emailAddress;

    @JsonIgnore
    @OneToMany(orphanRemoval = true,mappedBy = "uzytkownik")
    @JsonBackReference
    private Set<Opinion> opinions;

    @JsonIgnore
    @OneToMany(orphanRemoval = true,mappedBy = "uzytkownik")
    @JsonBackReference
    private Set<OpinionLike> opinionLikes;

    @JsonIgnore
    @OneToMany(orphanRemoval = true,mappedBy = "uzytkownik")
    @JsonBackReference
    private Set<Wideo> Wideos;




    public Uzytkownik(UUID randomUUID, String userName, String haslo, String emailAddress) {
        id=randomUUID;
        this.userName = userName;
        this.haslo=haslo;
        this.emailAddress=emailAddress;
        role="USER";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return haslo;
    }

    @Override
    public String getUsername() {
        return userName;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

//encja- w bazie danych
//repozytorium- baza danych
//serwis- komunikacja z bazą danych
//kontroler- tutaj requesty z fronta
