package com.echameunapata.backend.domain.enums.animals;

public enum AnimalSex {
    MALE,
    FEMALE,
    UNKNOWN;

    public static AnimalSex fromString(String sexString){
        for(AnimalSex sex: AnimalSex.values()){
            if(sex.name().equalsIgnoreCase(sexString)){
                return sex;
            }
        }
        return UNKNOWN;
    }

}
