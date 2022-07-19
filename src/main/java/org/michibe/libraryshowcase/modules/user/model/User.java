package org.michibe.libraryshowcase.modules.user.model;

import org.michibe.libraryshowcase.modules.user.persistence.UserEntity;
import org.springframework.lang.NonNull;

public record User(
        @NonNull
        UserId id,
        @NonNull
        String name,
        @NonNull
        String email,
        @NonNull
        String encryptedPassword,
        @NonNull
        Role role
) {
    public static User of(UserEntity entity) {
        return new User(
                UserId.ofUuid(entity.getId()),
                entity.getName(),
                entity.getEmail(),
                entity.getEncryptedPassword(),
                entity.getRole()
        );
    }
}
