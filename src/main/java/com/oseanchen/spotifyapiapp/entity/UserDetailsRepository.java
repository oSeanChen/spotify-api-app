package com.oseanchen.spotifyapiapp.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
    UserDetails findByRefId(String refId);
}
