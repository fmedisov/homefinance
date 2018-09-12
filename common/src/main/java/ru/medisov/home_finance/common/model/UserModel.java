package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user_tbl")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "userName", length = 36, nullable = false)
    private String userName;

    @Column(name = "email", length = 128, nullable = false)
    private String email;

    @Column(name = "firstName", length = 36)
    private String firstName;

    @Column(name = "lastName", length = 36)
    private String lastName;

    @Column(name = "encrytedPassword", length = 128, nullable = false)
    private String encrytedPassword;

    @Column(name = "enabled", length = 1, nullable = false)
    private boolean enabled;
}