package com.echameunapata.backend.domain.enums;

public enum ReportType {
    MALTREATMENT,
    ABANDONMENT,
    NEGLECT,
    MEDIC_EMERGENCY,
    OTHER;

    public static ReportType fromString(String typeString){
        for(ReportType type: ReportType.values()){
            if(type.name().equalsIgnoreCase(typeString)){
                return type;
            }
        }
        return OTHER;
    }
}
