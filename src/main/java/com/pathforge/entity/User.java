package com.pathforge.entity;

import com.pathforge.dto.UserDTO;
import com.pathforge.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is null or blank")
    private String fullName;

    @Column(unique = true)
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "email is null or blank")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = "Password must contain at least one uppercase letter, one digit, and be at least 6 characters long"
    )
    private String password;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;




}
