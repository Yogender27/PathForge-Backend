package com.pathforge.controllers;

import com.pathforge.dto.LoginDTO;
import com.pathforge.dto.UserDTO;
import com.pathforge.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO>registerUser( @Valid @RequestBody UserDTO userDTO){

        UserDTO newUser =userService.registerUser(userDTO);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<UserDTO>loginUser( @RequestBody @Valid LoginDTO loginDTO){

       UserDTO  newUser =userService.loginUser(loginDTO);

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
}
