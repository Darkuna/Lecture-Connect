package com.example.lecture_connect.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserxDTO {
    private int id;
    private String name;
    private String password;
    public UserxDTO() {
    }
    public UserxDTO(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
