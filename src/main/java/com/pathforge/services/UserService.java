package com.pathforge.services;

import com.pathforge.dto.LoginDTO;
import com.pathforge.dto.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO user);

    UserDTO loginUser(LoginDTO loginDTO);
}
