package com.pathforge.services;

import com.pathforge.dto.LoginDTO;
import com.pathforge.dto.ResponseDTO;
import com.pathforge.dto.UserDTO;
import com.pathforge.entity.User;
import com.pathforge.repository.UserRepository;
import com.pathforge.utility.Data;
import com.pathforge.utility.Utilities;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisServices redisServices;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already linked with other account");
        }

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encode the password
        user.setAccountType(userDTO.getAccountType());


        User savedUser = userRepository.save(user);
        return new UserDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                null, // Never expose password
                savedUser.getAccountType()
        );
    }

    @Override
    public UserDTO loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User does not exist with this email"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAccountType(user.getAccountType());

        return userDTO;
    }

    @Override
    public Boolean sentOtp(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User Does not exist"));
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mm,true);
        message.setTo(email);
        message.setSubject("Your Otp Code");

        String genOtp = Utilities.otpGenerate();
        redisServices.saveOtp(email,genOtp);
        message.setText(Data.getMessage(genOtp,user.getFullName()),true);
        mailSender.send(mm);
        return true;
    }

    @Override
    public Boolean verifyOtp(String email, String otp) {
        String savedOtp = redisServices.getOtp(email);

        if (savedOtp == null) {
            throw new RuntimeException("OTP expired or does not exist");
        }
        if (!savedOtp.equals(otp)) {
            throw new RuntimeException("OTP is incorrect");
        }

        redisServices.deleteOtp(email); //  delete after use

        return true;
    }


    @Override
    public ResponseDTO changePassword(LoginDTO loginDTO) {

        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()->
                new RuntimeException("User does not exits"));
        user.setPassword(loginDTO.getPassword());
        userRepository.save(user);
        return new ResponseDTO("Password changed successfully");
    }

}
