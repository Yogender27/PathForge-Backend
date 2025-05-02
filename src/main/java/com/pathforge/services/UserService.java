package com.pathforge.services;

import com.pathforge.dto.LoginDTO;
import com.pathforge.dto.ResponseDTO;
import com.pathforge.dto.UserDTO;
import jakarta.mail.MessagingException;

public interface UserService {
    UserDTO registerUser(UserDTO user);

    UserDTO loginUser(LoginDTO loginDTO);

    Boolean sentOtp(String email) throws Exception;

    Boolean verifyOtp(String email, String otp);

    ResponseDTO changePassword(LoginDTO loginDTO);
}
