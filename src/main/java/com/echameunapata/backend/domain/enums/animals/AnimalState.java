package com.echameunapata.backend.domain.enums.animals;

public enum AnimalState {
    AVAILABLE,
    UNDER_TREATMENT,
    UNDER_ADOPTION,
    ADOPTED,
    SPONSORED,
    FOSTER_HOME,
    DECEASED;

    public static AnimalState fromString(String stateString){
        for(AnimalState state: AnimalState.values()){
            if (state.name().equalsIgnoreCase(stateString)){
                return state;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + stateString);
    }
}
