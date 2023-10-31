package com.project.pet.user.dto.pet;

import com.project.pet.user.model.pet.Pet;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PetRequset {
    private String name;

    private String species;

    private String gender;

}
