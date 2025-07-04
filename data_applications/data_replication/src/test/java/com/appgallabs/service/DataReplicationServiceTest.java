package com.appgallabs.service;

import com.appgallabs.dataplatform.util.JsonUtil;
import com.appgallabs.dataplatform.util.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@QuarkusTest
public class DataReplicationServiceTest {
    private static Logger logger = LoggerFactory.getLogger(DataReplicationServiceTest.class);

    @Inject
    private DataReplicationService dataReplicationService;

    @Test
    public void ingest() throws Exception{
        String datasetLocation = "dataset/data.json";
        String json = Util.loadResource(datasetLocation);
        JsonElement datasetElement = JsonUtil.validateJson(json);
        logger.info("*****DATA_SET******");
        JsonUtil.printStdOut(datasetElement);


        JsonArray replicationPipeline = this.discoverReplicationPipeLine();
       this.dataReplicationService.ingest(
               10,
               datasetElement,
               replicationPipeline
       );
    }

    //-------------------------------------------------------------------------------
    private JsonArray discoverReplicationPipeLine(){
        try {
            JsonArray replicationPipes = new JsonArray();

            String configLocation = "pipe_config/pipe_config.json";
            String pipeConfigJson = Util.loadResource(configLocation);
            JsonArray pipesJsonArray = JsonUtil.validateJson(pipeConfigJson).getAsJsonArray();
            for(int i=0; i<pipesJsonArray.size(); i++) {
                JsonObject configJson = pipesJsonArray.get(i).getAsJsonObject();
                logger.info("*****PIPE_CONFIGURATION******");
                JsonUtil.printStdOut(configJson);
                replicationPipes.add(configJson);
            }

            return replicationPipes;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
