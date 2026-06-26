package com.prestige.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    // Статические фабричные методы для тестовых пользователей
    public static User admin() {
        return User.builder()
                .username("admin")
                .password("g@z")
                .build();
    }

    public static User lockedOutUser() {
        return User.builder()
                .username("locked_out_user")
                .password("secret_sauce")
                .build();
    }

    public static User problemUser() {
        return User.builder()
                .username("problem_user")
                .password("secret_sauce")
                .build();
    }

    public static User invalidUser() {
        return User.builder()
                .username("invalid_user")
                .password("wrong_password")
                .build();
    }
}