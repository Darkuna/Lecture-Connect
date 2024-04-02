package com.example.lecture_connect.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String name;
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
