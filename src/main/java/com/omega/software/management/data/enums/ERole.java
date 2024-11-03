package com.omega.software.management.data.enums;

import java.util.Set;

public enum ERole {
    ROLE_SENSOR_PROVIDER,
    ROLE_VIEWER,
    ROLE_MODERATOR,
    ROLE_MANAGER;

    public static boolean isNotValid(Set<String> roles) {
        boolean noneMatch = roles.stream().noneMatch(it -> isValidEnum(ERole.class, it));
        return noneMatch;
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
        return getEnum(enumClass, enumName, null) != null;
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName, E defaultEnum) {
        if (enumName == null) {
            return defaultEnum;
        } else {
            try {
                return Enum.valueOf(enumClass, enumName);
            } catch (IllegalArgumentException var4) {
                return defaultEnum;
            }
        }
    }
}
