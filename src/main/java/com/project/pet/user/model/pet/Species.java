package com.project.pet.user.model.pet;

import com.project.pet.global.common.exception.ErrorType;
import com.project.pet.user.exception.pet.SpeciesNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter @RequiredArgsConstructor
public enum Species {
    MALTESE("말티즈"),
    POODLE("푸들"),
    POMERANIAN("포메라니안"),
    MIXED_DOG("믹스견"),
    CHIHUAHUA("치와와"),
    SHIH_CHU("시츄"),
    GOLDEN_RETRIEVER("골든리트리버"),
    JINDO_DOG("진돗개");

    private final String value;
    public static Species of(String string){
        Species result = Arrays.stream(Species.values())
                .filter(species -> species.value.equals(string))
                .findFirst()
                .orElseThrow(() -> new SpeciesNotFoundException(ErrorType.NotFound.PET_SPECIES_NOT_FOUND, "해당 종이 존재하지 않습니다."));
        return result;
    }
}
