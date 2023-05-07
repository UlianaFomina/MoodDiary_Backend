package com.mood.diary.service.auth;

import com.mood.diary.service.AbstractServiceTest;
import com.mood.diary.service.auth.service.email.parse.EmailParseTemplateService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailParseTemplateServiceTest extends AbstractServiceTest {

    @Autowired
    EmailParseTemplateService emailParseService;

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
    void getVerificationEmailTemplate() {
        String username = "randomUsername";
        String link = "http://127.0.0.1/";

        String verificationEmail = emailParseService.getVerificationTemplate(username, link);

        assertThat(verificationEmail).contains(username);
        assertThat(verificationEmail).contains(link);
    }

    @Test
    void getResetPasswordTemplate() {
        String email = "randomEmail@gmail.com";
        String link = "http://127.0.0.1/reset";

        String verificationEmail = emailParseService.getResetPasswordTemplate(email, link);

        assertThat(verificationEmail).contains(email);
        assertThat(verificationEmail).contains(link);
    }
}
