package com.project.pet.pet.model;

import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
public class Pet {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long petId;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = LAZY)
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = STRING)
    private Species species;

    @Enumerated(value = STRING)
    private Gender gender;


}
