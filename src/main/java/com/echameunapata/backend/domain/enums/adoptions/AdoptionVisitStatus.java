package com.echameunapata.backend.domain.enums.adoptions;

public enum AdoptionVisitStatus {
    SCHEDULED,     // Programada
    COMPLETED,     // Ya se realizó
    CANCELLED;     //cancelada

    public static AdoptionVisitStatus fromString(String resultString){
        for(AdoptionVisitStatus result: AdoptionVisitStatus.values()){
            if(result.name().equalsIgnoreCase(resultString)){
                return result;
            }
        }

        throw  new IllegalArgumentException("No enum constant with text " + resultString);
    }


}
