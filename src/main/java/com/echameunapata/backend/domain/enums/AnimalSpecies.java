package com.echameunapata.backend.domain.enums;

public enum AnimalSpecies {
    DOG,
    CAT,
    RABBIT,
    BIRD,
    OTHER;

    public static AnimalSpecies fromString(String speciesString){
        for(AnimalSpecies species: AnimalSpecies.values()){
            if(species.name().equalsIgnoreCase(speciesString)){
                return species;
            }
        }
        return OTHER;
    }
}
