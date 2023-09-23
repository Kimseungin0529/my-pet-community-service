package com.project.pet.pet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
}
