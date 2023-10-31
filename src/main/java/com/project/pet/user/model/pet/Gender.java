package com.project.pet.user.model.pet;

import com.project.pet.global.common.exception.ErrorType;
import com.project.pet.user.exception.pet.GenderNotFoundException;
import com.project.pet.user.exception.pet.SpeciesNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter @RequiredArgsConstructor
public enum Gender {
    MALE("수컷"),
    FEMALE("암컷");

    private final String value;

    public static Gender of(String string) {
        Gender result = Arrays.stream(Gender.values())
                .filter(gender -> gender.value.equals(string))
                .findFirst()
                .orElseThrow(() -> new GenderNotFoundException(ErrorType.NotFound.PET_GENDER_NOT_FOUND, "해당 성별이 존재하지 않습니다."));
        return result;
    }
}
