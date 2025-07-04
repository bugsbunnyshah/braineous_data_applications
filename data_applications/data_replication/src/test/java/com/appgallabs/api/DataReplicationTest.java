package com.appgallabs.api;

import com.google.gson.JsonObject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DataReplicationTest {

    @Test
    public void ingest() throws Exception {
        JsonObject message = new JsonObject();
        message.addProperty("message", "DATA_INGESTION_SUCCESS");


        given()
                .when().body("{}").post("/ingest")
                .then()
                .statusCode(200)
                .body(is(message.toString()));

    }
}
