package com.example.lecture_connect.services;

import com.example.lecture_connect.LoginMessage;
import com.example.lecture_connect.dto.LoginDTO;
import com.example.lecture_connect.dto.UserxDTO;
import com.example.lecture_connect.models.Userx;

import java.util.List;
import java.util.Optional;

public interface UserxService {
    String adduserx(UserxDTO userxDTO);
    LoginMessage loginuserx(LoginDTO loginDTO);
    List<UserxDTO> getAllUsers();
}

