package com.project.pet.user.model.pet;

import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Embeddable @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Column(name = "pet_name")
    private String name;

    @Column(name = "pet_species")
    @Enumerated(value = STRING)
    private Species species;

    @Column(name = "pet_gender")
    @Enumerated(value = STRING)
    private Gender gender;

    @Builder
    public Pet(String name, Species species, Gender gender) {
        this.name = name;
        this.species = species;
        this.gender = gender;
    }

    public void registerPet(Pet pet){
        this.name = pet.getName();
        this.species = pet.getSpecies();
        this.gender = pet.getGender();
    }



}
