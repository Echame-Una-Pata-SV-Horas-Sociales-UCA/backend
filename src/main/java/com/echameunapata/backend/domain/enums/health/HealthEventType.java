package com.echameunapata.backend.domain.enums.health;

import com.echameunapata.backend.domain.models.HealthEvent;

public enum HealthEventType {
    VACCINE,
    DEWORMING,
    CONSULTATION,
    SURGERY,
    OBSERVATION,
    WEIGHT;

    public static HealthEventType fromString(String eventstring){
        for(HealthEventType event: HealthEventType.values()){
            if (event.name().equalsIgnoreCase(eventstring)){
                return event;
            }
        }

        throw new IllegalArgumentException("No enum constant with text " + eventstring);
    }
}
