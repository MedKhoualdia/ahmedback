package com.dance.mo.Entities.Enumarations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_READ_ONE("admin:readone"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    AFFECT_USER_TO_ROLE("affectUserToRole"),
    ;

    @Getter
    @Setter
    private final String permission;
}
