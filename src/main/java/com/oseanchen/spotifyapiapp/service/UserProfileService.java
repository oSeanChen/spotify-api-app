package com.oseanchen.spotifyapiapp.service;

import com.oseanchen.spotifyapiapp.entity.UserDetails;
import com.oseanchen.spotifyapiapp.entity.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserProfileService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public UserDetails insertOrUpdateUserDetails(User user, String accessToken, String refreshToken) {
        UserDetails userDetails = new UserDetails();
        userDetails.setAccessToken(accessToken);
        userDetails.setRefreshToken(refreshToken);
        userDetails.setRefId(generateUniqueId(user.getId()));
        return userDetailsRepository.save(userDetails);
    }

    public static String generateUniqueId(String input) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String combinedString = input + timeStamp;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combinedString.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
