package org.michibe.libraryshowcase.modules.library.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.michibe.libraryshowcase.modules.user.model.Role;
import org.michibe.libraryshowcase.modules.user.model.UserId;
import org.michibe.libraryshowcase.modules.user.persistence.UserEntity;
import org.michibe.libraryshowcase.modules.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository cut;

    @BeforeEach
    public void cleanup() {
        cut.deleteAll();
    }

    @Test
    public void saveAndGet() {
        final var user = new UserEntity(
                UserId.createNew().getValue(),
                "Max",
                "max-mustermann@gmail.com",
                "Password",
                Role.CUSTOMER
        );

        final var createdUser = this.cut.save(user);

        assertThat(createdUser.getId()).isEqualTo(user.getId());
        assertThat(createdUser.getName()).isEqualTo(user.getName());
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getEncryptedPassword()).isEqualTo(user.getEncryptedPassword());
        assertThat(createdUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    public void findByEmail() {
        final var user = new UserEntity(
                UserId.createNew().getValue(),
                "Max",
                "max-mustermann@gmail.com",
                "Password",
                Role.CUSTOMER
        );
        this.cut.save(user);

        final var result = this.cut.findByEmail("max-mustermann@gmail.com");

        assertThat(result).isPresent();
        final var foundUser = result.get();
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getName()).isEqualTo(user.getName());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundUser.getEncryptedPassword()).isEqualTo(user.getEncryptedPassword());
        assertThat(foundUser.getRole()).isEqualTo(user.getRole());
    }
}