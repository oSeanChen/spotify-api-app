package com.oseanchen.spotifyapiapp.controller;


import com.oseanchen.spotifyapiapp.config.SpotifyConfig;
import com.oseanchen.spotifyapiapp.entity.UserDetails;
import com.oseanchen.spotifyapiapp.service.SpotifyService;
import com.oseanchen.spotifyapiapp.service.UserProfileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SpotifyController {

    @Autowired
    private SpotifyConfig spotifyConfig;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping("login")
    public void spotifyLogin(HttpServletResponse response) throws IOException {
        SpotifyApi object = spotifyConfig.getSpotifyObject();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = object.authorizationCodeUri()
                .scope(spotifyConfig.getScopes())
                .show_dialog(true)
                .build();

        final URI uri = authorizationCodeUriRequest.execute();
        System.out.println("uri = " + uri);
        response.sendRedirect(uri.toString());
    }

    @GetMapping(value = "callback")
    public ResponseEntity<UserDetails> getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
        SpotifyApi object = spotifyConfig.getSpotifyObject();

        AuthorizationCodeRequest authorizationCodeRequest = object.authorizationCode(userCode).build();
        User user = null;

        UserDetails userDetails = new UserDetails();
        GetCurrentUsersProfileRequest getCurrentUsersProfile = null;
        try {
            final AuthorizationCodeCredentials authorizationCode = authorizationCodeRequest.execute();

            object.setAccessToken(authorizationCode.getAccessToken());
            object.setRefreshToken(authorizationCode.getRefreshToken());

            getCurrentUsersProfile = object.getCurrentUsersProfile().build();
            user = getCurrentUsersProfile.execute();

            userDetails = userProfileService.insertOrUpdateUserDetails(user, authorizationCode.getAccessToken(), authorizationCode.getRefreshToken());
        } catch (Exception e) {
            System.out.println("Exception occured while getting user code: " + e);
        }

        response.sendRedirect("userTopTracks?refId=" + userDetails.getRefId());
        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @GetMapping("/logout")
    public String logoutHandler(final HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/userSavedAlbums")
    public String userSavedAlbums(@RequestParam String refId, Model model) {
        SavedAlbum[] savedAlbums = spotifyService.getCurrentUserSavedAlbum(refId);

        List<Map<String, String>> albumInfo = Arrays.stream(savedAlbums)
                .map(album -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("name", album.getAlbum().getName());
                    info.put("image", album.getAlbum().getImages()[0].getUrl()); // 假設使用第一張圖片
                    info.put("externalUrls", album.getAlbum().getExternalUrls().get("spotify"));
                    return info;
                })
                .collect(Collectors.toList());

        model.addAttribute("albumInfo", albumInfo);
        model.addAttribute("view", "album");
        model.addAttribute("refId", refId);
        return "layout";
    }

    @GetMapping("/userSavedTracks")
    public String userSavedTracks(@RequestParam String refId, Model model) {
        SavedTrack[] savedTracks = spotifyService.getCurrentUserSavedTracks(refId);

        List<Map<String, String>> tracksInfo = Arrays.stream(savedTracks)
                .map(track -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("name", track.getTrack().getName());
                    info.put("image", track.getTrack().getAlbum().getImages()[0].getUrl());
                    info.put("externalUrls", track.getTrack().getExternalUrls().get("spotify"));
                    return info;
                })
                .collect(Collectors.toList());

        model.addAttribute("tracksInfo", tracksInfo);
        model.addAttribute("view", "track");
        model.addAttribute("refId", refId);
        return "layout";
    }

    @GetMapping("/userTopArtists")
    public String userTopArtists(@RequestParam String refId, Model model) {
        Artist[] topArtists = spotifyService.getCurrentUserTopArtists(refId);

        List<Map<String, String>> artistsInfo = Arrays.stream(topArtists)
                .map(artist -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("name", artist.getName());
                    info.put("image", artist.getImages()[0].getUrl());
                    info.put("externalUrls", artist.getExternalUrls().get("spotify"));
                    return info;
                })
                .collect(Collectors.toList());

        model.addAttribute("artistsInfo", artistsInfo);
        model.addAttribute("view", "topArtist");
        model.addAttribute("refId", refId);
        return "layout";
    }

    @GetMapping("/userTopTracks")
    public String userTopTracks(@RequestParam String refId, Model model) {
        Track[] topTracks = spotifyService.getCurrentUserTopTracks(refId);

        List<Map<String, String>> tracksInfo = Arrays.stream(topTracks)
                .map(track -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("name", track.getName());
                    info.put("image", track.getAlbum().getImages()[0].getUrl());
                    info.put("externalUrls", track.getExternalUrls().get("spotify"));
                    return info;
                })
                .collect(Collectors.toList());

        model.addAttribute("tracksInfo", tracksInfo);
        model.addAttribute("view", "topTrack");
        model.addAttribute("refId", refId);
        return "layout";
    }

    @GetMapping("/recommendationTracks")
    public String recommendation(@RequestParam String refId, Model model) {
        Track[] recommendationTracks = spotifyService.getRecommendation(refId);
        List<Map<String, String>> tracksInfo = Arrays.stream(recommendationTracks)
                .map(track -> {
                    Map<String, String> info = new HashMap<>();
                    info.put("name", track.getName());
                    info.put("image", track.getAlbum().getImages()[0].getUrl());
                    info.put("externalUrls", track.getExternalUrls().get("spotify"));
                    return info;
                })
                .collect(Collectors.toList());

        model.addAttribute("tracksInfo", tracksInfo);
        model.addAttribute("view", "recommend");
        model.addAttribute("refId", refId);
        return "layout";
    }


}
