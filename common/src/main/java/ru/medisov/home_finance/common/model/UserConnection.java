package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@Entity
@Table(name = "user_connection_tbl")
public class UserConnection implements Serializable {

    private static final long serialVersionUID = -6991752510891411572L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "providerId", nullable = false)
    private String providerId;

    @Column(name = "providerUserId", nullable = false)
    private String providerUserId;

    @Column(name = "rate", nullable = false)
    private int rate;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "profileUrl", length = 512)
    private String profileUrl;

    @Column(name = "imageUrl", length = 512)
    private String imageUrl;

    @Column(name = "accessToken", length = 512)
    private String accessToken;

    @Column(name = "secret", length = 512)
    private String secret;

    @Column(name = "refreshToken", length = 512)
    private String refreshToken;

    @Column(name = "expireTime")
    private Long expireTime;
}