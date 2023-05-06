package com.mood.diary.service.user.service;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.UserAlreadyExistsException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.user.model.AuthUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AuthUserServiceTest extends AbstractServiceTest {

    @Autowired
    AuthUserService authUserService;

    @Test
    void getByUsername_exists() {
        String username = "username";
        String email = "email@gmail.com";
        initDefaultUser(username, email);

        AuthUser dbUser = authUserService.findByUsername(username);

        assertThat(dbUser.getEmail()).isEqualTo(email);
        assertThat(dbUser.getUsername()).isEqualTo(username);
    }

    @Test
    void getByUsername_throwException() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> authUserService.findByUsername("notExistUsername"));
    }

    @Test
    void getByEmail_exists() {
        String email = "email@gmail.com";
        String username = "username";
        initDefaultUser(username, email);

        AuthUser dbUser = authUserService.findByEmail(email);

        assertThat(dbUser.getUsername()).isEqualTo(username);
        assertThat(dbUser.getEmail()).isEqualTo(email);
    }

    @Test
    void getByEmail_throwException() {
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> authUserService.findByEmail("mail@mail.com"));
    }

    @Test
    void save() {
        String email = "email@gmail.com";
        String username = "username";
        AuthUser authUser = AuthUser
                .builder()
                .email(email)
                .username(username)
                .build();

        authUserService.save(authUser);

        AuthUser dbUser = authUserService.findByEmail(email);

        assertThat(dbUser.getUsername()).isEqualTo(username);
        assertThat(dbUser.getEmail()).isEqualTo(email);
    }

    @Test
    void validateUniqueUsername_throwException() {
        String username = "username";
        String email = "email@gmail.com";
        initDefaultUser(username, email);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> authUserService.validateUniqueUsernameAndEmail(username, "randomEmail@gmail.com"));
    }

    @Test
    void validateUniqueEmail_throwException() {
        String username = "username";
        String email = "email@gmail.com";
        initDefaultUser(username, email);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> authUserService.validateUniqueUsernameAndEmail(username, email));
    }

    @Test
    void validateUniqueEmail_pass() {
        String username = "username";
        String email = "email@gmail.com";
        initDefaultUser(username, email);

        assertThatNoException()
                .isThrownBy(() -> authUserService.validateUniqueUsernameAndEmail("randomUsername", "randomEmail@gmail.com"));
    }

    private void initDefaultUser(String username, String email) {
        AuthUser dbUser = AuthUser
                .builder()
                .username(username)
                .email(email)
                .build();

        authUserRepository.save(dbUser);
    }
}
