package com.echameunapata.backend.domain.enums.adoptions;

public enum AdoptionStatus {
    PENDING,
    IN_REVIEW,
    APPROVED,
    REJECTED,
    DELIVERED,
    FOLLOW_UP;

    public static AdoptionStatus fromString(String statusString){
        for(AdoptionStatus status: AdoptionStatus.values()){
            if(status.name().equalsIgnoreCase(statusString)){
                return status;
            }
        }
        return PENDING;
    }
}
