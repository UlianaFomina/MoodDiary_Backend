package com.mood.diary.service.user;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.exception.variants.UserAlreadyExistsException;
import com.mood.diary.service.auth.exception.variants.UserNotFoundException;
import com.mood.diary.service.user.model.AuthUser;
import com.mood.diary.service.user.service.AuthUserService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AuthUserServiceTest extends AbstractServiceTest {

    @Autowired
    AuthUserService authUserService;

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo:4.0.10");

    @Container
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        String redisHost = String.format("redis://%s:%s", redisContainer.getHost(), redisContainer.getMappedPort(6379));
        Supplier<Object> redisSupplier = () -> redisHost;

        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
        registry.add("redis.uri", redisSupplier);
    }

    @Test
    void getByUsername_exists() {
        String username = "username";
        String email = "email@gmail.com";
        initDefaultUser(username, email);

        AuthUser dbUser = authUserService.findByUsername(username);

        assertThat(dbUser.getUsername()).isEqualTo(username);
        assertThat(dbUser.getEmail()).isEqualTo(email);

        assertThat(dbUser.getPassword()).isNotNull();
        assertThat(dbUser.getAbout()).isNotNull();
        assertThat(dbUser.getImageUrl()).isNotNull();
        assertThat(dbUser.getRole()).isNotNull();
        assertThat(dbUser.getDateOfBirth()).isNotNull();

        assertThat(dbUser.getAuthorities().stream().map(GrantedAuthority::getAuthority))
                .containsExactlyInAnyOrder(dbUser.getRole().name());
        assertThat(dbUser.isAccountNonExpired()).isTrue();
        assertThat(dbUser.isAccountNonLocked()).isTrue();
        assertThat(dbUser.isCredentialsNonExpired()).isTrue();
        assertThat(dbUser.isEnabled()).isTrue();
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
        assertThat(dbUser.getPassword()).isNotNull();
        assertThat(dbUser.getAbout()).isNotNull();
        assertThat(dbUser.getImageUrl()).isNotNull();
        assertThat(dbUser.getRole()).isNotNull();
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
}
