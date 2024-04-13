package com.example.demo.services;

import com.example.demo.models.UserInfo;
import com.example.demo.models.Userx;
import com.example.demo.repositories.UserInfoRepository;
import com.example.demo.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService {
    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public String addUser(UserInfo user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
        return "User Added Successfully";
    }

    public UserInfo allowLogin(String name, String password){
        Optional<UserInfo> user = repository.findByName(name);
        if(user.isPresent() && encoder.matches(password, user.get().getPassword())){
            return user.get();
        }
        return null;
    }


}

