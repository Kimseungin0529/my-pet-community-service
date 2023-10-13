package com.project.pet.user.model.pet;

import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Embeddable @Getter
public class Pet {

    private String name;

    @Enumerated(value = STRING)
    private Species species;

    @Enumerated(value = STRING)
    private Gender gender;


}
