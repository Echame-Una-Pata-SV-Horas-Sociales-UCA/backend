package com.echameunapata.backend.domain.enums.user;

public enum RoleActions {
    REMOVE,
    ADD;

    public static RoleActions fromString(String action){
        for(RoleActions r: RoleActions.values()){
            if(r.name().equalsIgnoreCase(action)){
                return r;
            }
        }

        throw new IllegalArgumentException("No enum constant with text" + action);
    }
}
