package org.michibe.libraryshowcase.configuration.security;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor implements PasswordEncoder {

    private final StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

    @Override
    public String encode(CharSequence rawPassword) {
        return encryptor.encryptPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encryptor.checkPassword(rawPassword.toString(), encodedPassword);
    }
}
