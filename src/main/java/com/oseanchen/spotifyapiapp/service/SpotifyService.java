package com.oseanchen.spotifyapiapp.service;


import com.oseanchen.spotifyapiapp.config.SpotifyConfig;
import com.oseanchen.spotifyapiapp.entity.UserDetails;
import com.oseanchen.spotifyapiapp.entity.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;

import java.util.Arrays;

@Service
public class SpotifyService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private SpotifyConfig spotifyConfig;

    public SavedAlbum[] getCurrentUserSavedAlbum(String refId) {
        SpotifyApi object = getSpotifyObject(refId);

        final GetCurrentUsersSavedAlbumsRequest getUsersTopArtistsRequest = object.getCurrentUsersSavedAlbums()
                .limit(25)
                .offset(0)
                .build();

        try {
            final Paging<SavedAlbum> artistPaging = getUsersTopArtistsRequest.execute();
            System.out.println(Arrays.stream(artistPaging.getItems()).limit(1).findAny());

            return artistPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching user saved album: " + e);
        }

        return new SavedAlbum[0];
    }

    public SavedTrack[] getCurrentUserSavedTracks(String refId) {
        SpotifyApi object = getSpotifyObject(refId);

        final GetUsersSavedTracksRequest getUsersSavedTracksRequest = object.getUsersSavedTracks()
                .limit(25)
                .offset(0)
                .build();

        try {
            final Paging<SavedTrack> trackPaging = getUsersSavedTracksRequest.execute();
            System.out.println(Arrays.stream(trackPaging.getItems()).limit(1).findAny());

            return trackPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching user saved tracks: " + e);
        }
        return new SavedTrack[0];
    }
    public Artist[] getCurrentUserTopArtists(String refId) {
        SpotifyApi object = getSpotifyObject(refId);

        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = object.getUsersTopArtists()
                .limit(20)
                .offset(0)
                .time_range("short_term")
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            System.out.println(Arrays.stream(artistPaging.getItems()).limit(1).findAny());

            return artistPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching user top artists: " + e);
        }
        return new Artist[0];
    }



    private SpotifyApi getSpotifyObject(String refId) {
        UserDetails userDetails = userDetailsRepository.findByRefId(refId);

        SpotifyApi object = spotifyConfig.getSpotifyObject();
        object.setAccessToken(userDetails.getAccessToken());
        object.setRefreshToken(userDetails.getRefreshToken());
        return object;
    }

    public Track[] getCurrentUserTopTracks(String refId) {
        SpotifyApi object = getSpotifyObject(refId);

        final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
                .limit(20)
                .offset(0)
                .time_range("short_term")
                .build();

        try {
            final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            System.out.println(Arrays.stream(trackPaging.getItems()).limit(1).findAny());

            return trackPaging.getItems();
        } catch (Exception e) {
            System.out.println("Exception occured while fetching user top artists: " + e);
        }
        return new Track[0];
    }
}
