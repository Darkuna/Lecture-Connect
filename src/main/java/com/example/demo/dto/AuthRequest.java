package com.example.demo.dto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AuthRequest {
    private String name;
    private String password;
}