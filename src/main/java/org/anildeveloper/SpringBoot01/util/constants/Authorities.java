package org.anildeveloper.SpringBoot01.util.constants;

import lombok.Getter;

@Getter
public enum Authorities {
    RESET_ANY_USER_PASSWORD(1l,"RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l, "ACCESS_ADMIN_PANEL");
    
    Long id;
    String authorityName;

    private Authorities(Long id, String authorityName){
        this.id =id;
        this.authorityName=authorityName;
    }
    
}
