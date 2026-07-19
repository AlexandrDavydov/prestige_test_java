package com.prestige.models;

import com.prestige.config.TestConfig;
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

    public static User admin() {
        return User.builder()
                .username(TestConfig.getAppUsername())
                .password(TestConfig.getAppPassword())
                .build();
    }
}