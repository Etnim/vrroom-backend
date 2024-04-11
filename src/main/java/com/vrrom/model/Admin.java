package com.vrrom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "applications")
    @OneToMany(mappedBy = "manager")
    private List<Application> applications;
}
