package org.michibe.libraryshowcase.modules.user;

import org.hibernate.exception.ConstraintViolationException;
import org.michibe.libraryshowcase.api.model.CreateUserReqPayload;
import org.michibe.libraryshowcase.api.model.UpdateUserReqPayload;
import org.michibe.libraryshowcase.configuration.security.PasswordEncryptor;
import org.michibe.libraryshowcase.modules.user.model.User;
import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.michibe.libraryshowcase.modules.user.persistence.UserEntity;
import org.michibe.libraryshowcase.modules.user.persistence.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record UserService(
        UserRepository userRepository,
        PasswordEncryptor passwordEncryptor
) {
    public List<User> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(User::of)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserByEmail(@NonNull String email) {
        return userRepository.
                findByEmail(email)
                .map(User::of);
    }

    public Optional<User> getUser(@NonNull UserId userId) {
        return userRepository.
                findById(userId.getValue())
                .map(User::of);
    }

    @NonNull
    public User createUser(@NonNull CreateUserReqPayload payload) {
        final var encryptedPassword = passwordEncryptor.encode(payload.password());
        final var createdUser = createOrUpdateUser(new UserEntity(
                UserId.createNew().getValue(),
                payload.name(),
                payload.email(),
                encryptedPassword,
                payload.role()
        ));
        return User.of(createdUser);
    }


    public User updateUser(
            @NonNull UserId id,
            @NonNull UpdateUserReqPayload payload
    ) {
        final var user = userRepository.findById(id.getValue());
        final var encryptedPassword = passwordEncryptor.encode(payload.password());

        if (user.isEmpty()) {
            throw new UserNotExistException(id);
        }

        final var updatedUser = createOrUpdateUser(new UserEntity(
                user.get().getId(),
                payload.name(),
                user.get().getEmail(),
                encryptedPassword,
                payload.role()
        ));


        return User.of(updatedUser);
    }

    public void deleteUser(@NonNull UserId id) {
        final var user = this.userRepository.findById(id.getValue());
        if (user.isPresent()) {
            this.userRepository.delete(user.get());
        } else {
            throw new UserNotExistException(id);
        }
    }


    private UserEntity createOrUpdateUser(@NonNull UserEntity userEntity) {
        try {
            return userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            final var cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                if (((ConstraintViolationException) cause).getSQLException().getMessage().contains("UC__USERS__EMAIL")) {
                    throw new UserAlreadyExistException(userEntity.getEmail());
                }
            }
            throw e;
        }
    }

}