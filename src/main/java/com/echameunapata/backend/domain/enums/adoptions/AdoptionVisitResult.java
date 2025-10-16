package com.echameunapata.backend.domain.enums.adoptions;

public enum AdoptionVisitResult {
    NOT_SUITABLE,
    SUITABLE,
    REQUIRED_ADJUSTMENTS;

    public static AdoptionVisitResult fromString(String resultString){
        for(AdoptionVisitResult result: AdoptionVisitResult.values()){
            if(result.name().equalsIgnoreCase(resultString)){
                return result;
            }
        }

        throw  new IllegalArgumentException("No enum constant with text " + resultString);
    }


}
