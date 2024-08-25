package com.oseanchen.spotifyapiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpotifyApiAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyApiAppApplication.class, args);
    }

}
