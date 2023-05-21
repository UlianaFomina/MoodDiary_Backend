package com.mood.diary.service.standfort;

import com.mood.diary.service.AbstractServiceTest;
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
class AnalyzeServiceTest extends AbstractServiceTest {

    @Autowired
    AnalyzeService analyzeService;

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo:4.0.10");

    @Container
    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        String redisHost = String.format("redis://%s:%s", redisContainer.getHost(), redisContainer.getMappedPort(6379));
        Supplier<Object> redisSupplier = () -> redisHost;
        Supplier<Object> stanfordSupplier = () -> "tokenize, ssplit, parse, sentiment";

        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
        registry.add("redis.uri", redisSupplier);
        registry.add("annotators", stanfordSupplier);
    }

    @Test
    void satisfaction_veryNegative() {
        String text = """
                I hate this product.
                It completely disappointed me and fails to deliver on its promises.
                """;

        assertSatisfaction(text, -2.0, -1.0);
    }

    @Test
    void satisfaction_negative() {
        String text = """
                This book was a letdown.
                The storytelling was boring, and the characters were uninteresting.
                """;

        assertSatisfaction(text, -1.0, 0);
    }

    @Test
    void satisfaction_neutral() {
        String text = """
                This movie was average.
                Nothing particularly outstanding, but not bad either
                """;

        assertSatisfaction(text, 0.0, 1.0);
    }

    @Test
    void satisfaction_positive() {
        String text = """
                This restaurant provided an excellent dining experience.
                The food was delicious, and the staff was friendly.
                """;

        assertSatisfaction(text, 1.0, 2.0);
    }

    @Test
    void satisfaction_veryPositive() {
        String text = """
                This movie was simply amazing!
                It captivates the audience from the first minutes, and the actors delivered superb performances
                """;

        assertSatisfaction(text, 1.0, 2.0);
    }

    private void assertSatisfaction(String text, double minRate, double maxRate) {
        double satisfaction = analyzeService.satisfaction(text);

        assertThat(satisfaction).isBetween(minRate, maxRate);
    }
}
