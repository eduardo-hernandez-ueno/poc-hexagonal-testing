package com.example.infrastructure;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ProductsBaseIT {

    @LocalServerPort
    private Integer port;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Container
    @ServiceConnection
    protected static MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Container
    protected static WireMockContainer wireMockContainer =
            new WireMockContainer("wiremock/wiremock")
                    .withClasspathResourceMapping("wiremock/__files", "/home/wiremock", BindMode.READ_ONLY);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("test.clients.amazon.url", () -> wireMockContainer.getBaseUrl() + "/amazon");
        registry.add("test.clients.ebay.url", () -> wireMockContainer.getBaseUrl() + "/ebay");
    }

    @PostConstruct
    private void configure() {
        RestAssured.port = port;
    }

    @AfterEach
    void tierDown() {
        mongoTemplate.getCollectionNames().forEach(s -> mongoTemplate.getCollection(s).drop());
    }

}
