package com.oseanchen.spotifyapiapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "USER_DETAILS")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserDetails implements Serializable {
    private static final long serialVersionUID = 3937414011943770889L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "ref_id")
    private String refId;

    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

}