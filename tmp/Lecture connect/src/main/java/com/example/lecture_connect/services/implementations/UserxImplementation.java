package com.example.lecture_connect.services.implementations;

import com.example.lecture_connect.dto.LoginDTO;
import com.example.lecture_connect.dto.UserxDTO;
import com.example.lecture_connect.models.Userx;
import com.example.lecture_connect.repository.UserxRepository;
import com.example.lecture_connect.services.UserxService;
import com.example.lecture_connect.LoginMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserxImplementation implements UserxService {
    private final UserxRepository userxRepository;
    private final PasswordEncoder passwordEncoder;

    UserxDTO userxDTO;

    public UserxImplementation(UserxRepository userxRepository, PasswordEncoder encoder) {
        this.userxRepository = userxRepository;
        this.passwordEncoder = encoder;
    }

    @Override
    public String adduserx(UserxDTO userxDTO) {
        Userx user = new Userx(
                userxDTO.getName(),
                this.passwordEncoder.encode(userxDTO.getPassword())
        );
        userxRepository.save(user);
        return user.getName();
    }
    @Override
    public LoginMessage  loginuserx(LoginDTO loginDTO) {
        Userx userx1 = userxRepository.findByName(loginDTO.getName());
        if (userx1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = userx1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<Userx> userx = userxRepository.findOneByNameAndPassword(loginDTO.getName(), encodedPassword);
                if (userx.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("password Not Match", false);
            }
        }else {
            return new LoginMessage("Email not exits", false);
        }
    }

    @Override
    public List<UserxDTO> getAllUsers() {
        List<Userx> users = userxRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserxDTO convertToDTO(Userx user) {
        UserxDTO dto = new UserxDTO();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        // Setze weitere Attribute entsprechend
        return dto;
    }
}
