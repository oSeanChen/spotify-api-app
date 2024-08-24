package com.oseanchen.spotifyapiapp.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.enums.AuthorizationScope;

import java.net.URI;

@Service
public class SpotifyConfig {


    @Value("${redirect.server.ip}")
    private String customIp;

    @Value("${spotify.client.ip}")
    private String clientIp;

    @Value("${spotify.client.secret}")
    private String clentSecret;

    public SpotifyApi getSpotifyObject(){
        URI rediectUrl = SpotifyHttpManager.makeUri(customIp + "/callback");

        return new SpotifyApi
                .Builder()
                .setClientId(clientIp)
                .setClientSecret(clentSecret)
                .setRedirectUri(rediectUrl)
                .build();
    }

    public AuthorizationScope[] getScopes() {
        return new AuthorizationScope[] {
                AuthorizationScope.USER_TOP_READ,
                AuthorizationScope.USER_LIBRARY_READ
        };
    }
}
