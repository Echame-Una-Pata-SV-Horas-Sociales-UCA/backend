package com.echameunapata.backend.domain.enums.reports;

public enum ReportStatus {
    PENDING,
    IN_PROGRESS,
    RESOLVED,
    REJECTED;

    public static ReportStatus fromString(String statusString){
        for(ReportStatus status: ReportStatus.values()){
            if(status.name().equalsIgnoreCase(statusString)){
                return status;
            }
        }
        return PENDING;
    }
}
