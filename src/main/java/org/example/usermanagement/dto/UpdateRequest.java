package org.example.usermanagement.dto;

import lombok.Data;

@Data
public class UpdateRequest {

    private String email;
    private String password;
}
