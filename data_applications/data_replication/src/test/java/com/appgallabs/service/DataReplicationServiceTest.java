package com.appgallabs.service;

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

import javax.inject.Inject;

@QuarkusTest
public class DataReplicationServiceTest {
    private static Logger logger = LoggerFactory.getLogger(DataReplicationServiceTest.class);

    @Inject
    private DataReplicationService dataReplicationService;

    @Test
    public void ingest() throws Exception{
        //dataset
        JsonElement datasetElement = PayloadUtil.dataset();

        //replication_pipeline
        JsonArray replicationPipeline = PayloadUtil.discoverReplicationPipeLine();

       this.dataReplicationService.ingest(
               10,
               datasetElement,
               replicationPipeline
       );
    }
}
