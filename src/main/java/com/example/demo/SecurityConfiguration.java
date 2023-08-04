package com.example.demo;

import com.example.demo.Entity.Uzytkownik;
import com.example.demo.Repository.UzytkownikRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.util.UUID;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private UzytkownikDetailServiceImpl uzytkownikDetailService;

    private UzytkownikRepository uzytkownikRepository;

    public SecurityConfiguration(UzytkownikDetailServiceImpl uzytkownikDetailService) {
        this.uzytkownikDetailService = uzytkownikDetailService;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
        http
                .headers().disable();


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/test2").permitAll();

        http.authorizeHttpRequests().requestMatchers("/test").hasAuthority("USER")
                .and()
                .formLogin() // umożliwij logowanie za pomocą formularza
                .loginPage("/login").successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        Uzytkownik uzytkownik= (Uzytkownik) authentication.getPrincipal();
                        UUID uuid=uzytkownik.getId();
                        String id= String.valueOf(uuid);
                        String rola=uzytkownik.getRole();
                        String name=uzytkownik.getUsername();

                        Cookie cookie=new Cookie("id_zalogowany", id );
                        cookie.setMaxAge(60*60*24);
                        response.addCookie(cookie);

                        Cookie cookie1=new Cookie("rola", rola);
                        cookie1.setMaxAge(60*60*24);
                        response.addCookie(cookie1);

                        Cookie cookie3=new Cookie("nazwa_zalogowanego", name);
                        cookie3.setMaxAge(60*60*24);
                        response.addCookie(cookie3);

                        //strona startowa
                        response.sendRedirect("/wszystkiewideo.html");

                    }
                });


                        http.logout().deleteCookies().addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        Cookie[] cookies=request.getCookies();
                        for (Cookie cookie: cookies)
                        {
                            if (cookie.getName().equals("rola"))
                            {
                                cookie.setMaxAge(0);
                                response.addCookie(cookie);
                            }

                            if (cookie.getName().equals("id_zalogowany"))
                            {
                                cookie.setMaxAge(0);
                                response.addCookie(cookie);
                            }
                            if (cookie.getName().equals("nazwa_zalogowanego "))
                            {
                                cookie.setMaxAge(0);
                                response.addCookie(cookie);
                            }
                        }

                        try {

                            //strona startowa
                            response.sendRedirect("/wszystkiewideo.html");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
        http.authorizeHttpRequests().requestMatchers("/**").permitAll();

        http.httpBasic();
        return http.build();
    }


}