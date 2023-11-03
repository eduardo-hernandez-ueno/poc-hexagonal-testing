package com.example.infrastructure.controller;

import com.example.infrastructure.ProductsBaseIT;
import com.example.infrastructure.data.repositories.holders.ProductDataHolder;
import io.restassured.RestAssured;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class ProductControllerIT extends ProductsBaseIT {

    @Test
    void findByCode_whenExistsProduct_returnProduct() {
        mongoTemplate.save(new ProductDataHolder(null, "123456", "asd", BigDecimal.TEN, Map.of("tag1", "value1", "tag2", "value2")));

        String jsonResponse = RestAssured
                .when().get("/products/{code}", "123456")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().asPrettyString();

        assertThatJson(jsonResponse)
                .isEqualTo("""
                           {
                            "code": "123456",
                            "name": "asd",
                            "price": 10,
                            "tags": {
                                "tag1": "value1",
                                "tag2": "value2"
                            }
                           }
                        """);
    }
    
    @Test
    void findByCode_whenNotExistsProduct_returnNotFound() {
        RestAssured
                .when().get("/products/{code}", "123456")
                .then()
                .assertThat()
                .statusCode(404);
    }


    @Test
    void create_whenNotExistsProduct_returnProductCreated() {
        long dbBefore = mongoTemplate.getCollection("products").countDocuments();

        String responseBody = RestAssured
                .given().body("""
                             {
                            "code": "123456",
                            "name": "asd",
                            "price": 10
                           }
                        """).and().contentType("application/json")
                .when().post("/products")
                .then()
                .assertThat()
                .statusCode(201)
                .extract().asPrettyString();

        assertThatJson(responseBody)
                .isEqualTo("""
                           {
                            "code": "123456",
                            "name": "asd",
                            "price": 10,
                            "tags": {
                                "tag1": "value1",
                                "tag2": "value2"
                            }
                           }
                        """);
        long dbAfter = mongoTemplate.getCollection("products").countDocuments();
        assertThat(dbBefore).isEqualTo(0);
        assertThat(dbAfter).isEqualTo(1);
    }

    @Test
    void create_whenExistsProduct_returnConflict() {
        ProductDataHolder persistedProduct = new ProductDataHolder(null, "123456", "qwe", BigDecimal.ONE, Map.of());
        mongoTemplate.save(persistedProduct);
        RestAssured
                .given().body("""
                             {
                            "code": "123456",
                            "name": "asd",
                            "price": 10
                           }
                        """).and().contentType("application/json")
                .when().post("/products")
                .then()
                .assertThat()
                .statusCode(409);

        List<ProductDataHolder> products = mongoTemplate.findAll(ProductDataHolder.class);
        assertThat(products).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(persistedProduct);
    }
}
