package com.vrrom.admin.dtos;

import com.vrrom.admin.model.Admin.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private String uid;
    private String email;
    private String name;
    private String surname;
    private AdminRole role;
}