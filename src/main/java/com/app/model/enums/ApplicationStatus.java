package com.app.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum ApplicationStatus{
    REGISTERED("Зарегистрирована"),
    SOLVED("Выполнена"),
    ;

    private final String name;
}

