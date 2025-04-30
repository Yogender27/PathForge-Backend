package com.pathforge.dto;

import com.pathforge.entity.User;
import com.pathforge.enums.AccountType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String fullName;
    private String email;
    private String password;

    private AccountType accountType;

}
