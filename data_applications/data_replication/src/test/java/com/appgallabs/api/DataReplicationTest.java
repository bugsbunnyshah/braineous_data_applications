package com.appgallabs.api;

import com.appgallabs.dataplatform.util.JsonUtil;
import com.appgallabs.dataplatform.util.Util;
import com.appgallabs.reuse.PayloadUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DataReplicationTest {
    private static Logger logger = LoggerFactory.getLogger(DataReplicationTest.class);

    @Test
    public void ingest() throws Exception {
        JsonObject message = new JsonObject();
        message.addProperty("message", "DATA_INGESTION_SUCCESS");

        //payload
        JsonObject payload = new JsonObject();
        payload.addProperty("stream_size_in_objects", 2);

        //replication_pipeline
        JsonArray replicationPipeline = PayloadUtil.discoverReplicationPipeLine();
        payload.add("replication_pipeline", replicationPipeline);

        //dataset
        JsonElement datasetElement = PayloadUtil.dataset();
        payload.add("dataset", datasetElement);

        //TODO: REMOVE: inspect_payload
        logger.info("***PAYLOAD***");
        JsonUtil.printStdOut(payload);

        given()
                .when().body(payload.toString()).post("/ingest")
                .then()
                .statusCode(200)
                .body(is(message.toString()));

    }
}
