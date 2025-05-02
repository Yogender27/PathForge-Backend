package com.pathforge.controllers;

import com.pathforge.dto.LoginDTO;
import com.pathforge.dto.ResponseDTO;
import com.pathforge.dto.UserDTO;
import com.pathforge.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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

    @GetMapping("/sendOtp/{email}")
    public ResponseEntity<ResponseDTO>sendOtp(@PathVariable @Email (message = "Email is invalid") String email) throws Exception {

        userService.sentOtp(email);
        return new ResponseEntity<>(new ResponseDTO("OTP Sent Successfully"), HttpStatus.OK);
    }

    @GetMapping("/verifyOtp/{email}/{otp}")
    public ResponseEntity<ResponseDTO>verifyOtp( @PathVariable  @Email( message = "Email is invalid") String email,@PathVariable @Pattern(regexp = "^\\d{6}$", message = "OTP is invalid") String otp) throws Exception {

        userService.verifyOtp(email,otp);
        return new ResponseEntity<>(new ResponseDTO("OTP verified Successfully"), HttpStatus.OK);
    }

    @PostMapping("/changePass")
    public ResponseEntity<ResponseDTO>changePassword( @RequestBody @Valid LoginDTO loginDTO){

         ResponseDTO result = userService.changePassword(loginDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
