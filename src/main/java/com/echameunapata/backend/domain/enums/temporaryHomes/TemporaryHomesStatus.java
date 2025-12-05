package com.echameunapata.backend.domain.enums.temporaryHomes;

public enum TemporaryHomesStatus {
    ACTIVE,
    FINISHED,
    CANCELLED,
    PENDING;

    public static TemporaryHomesStatus fromString(String status) {
        for (TemporaryHomesStatus s : TemporaryHomesStatus.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant with text " + status);
    }
}
