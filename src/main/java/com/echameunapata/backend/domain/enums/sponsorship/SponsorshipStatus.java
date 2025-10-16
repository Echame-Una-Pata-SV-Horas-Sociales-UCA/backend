package com.echameunapata.backend.domain.enums.sponsorship;

public enum SponsorshipStatus {
    ACTIVE,
    INACTIVE,
    PENDING;

    public static SponsorshipStatus fromString(String statusString){
        for(SponsorshipStatus status: SponsorshipStatus.values()){
            if(status.name().equalsIgnoreCase(statusString)){
                return status;
            }
        }
        return PENDING;
    }
}
