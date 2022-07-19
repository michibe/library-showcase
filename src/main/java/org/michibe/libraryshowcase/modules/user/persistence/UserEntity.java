package org.michibe.libraryshowcase.modules.user.persistence;


import org.michibe.libraryshowcase.modules.user.model.Role;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.michibe.libraryshowcase.modules.user.persistence.UserEntity.TABLE_NAME;


@Entity
@Table(name = TABLE_NAME)
public class UserEntity {
    public static final String TABLE_NAME = "users";

    @Id
    @NotNull
    @NonNull
    private UUID id;

    @NotEmpty
    @NonNull
    private String name;

    @NotEmpty
    @Email
    @NonNull
    private String email;
    @NotNull
    @NonNull
    private String encryptedPassword;

    @Enumerated(EnumType.STRING)
    @NotNull
    @NonNull
    private Role role;

    public UserEntity() {
        super();
    }

    public UserEntity(
            @NonNull UUID id,
            @NonNull String name,
            @NonNull String email,
            @NonNull String encryptedPassword,
            @NonNull Role role
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    @NonNull
    public Role getRole() {
        return role;
    }
}
