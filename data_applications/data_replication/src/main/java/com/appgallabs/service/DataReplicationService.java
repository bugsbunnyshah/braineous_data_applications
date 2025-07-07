package com.appgallabs.service;

import com.appgallabs.dataplatform.client.sdk.api.Configuration;
import com.appgallabs.dataplatform.client.sdk.api.DataPlatformService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DataReplicationService {
    private static Logger logger = LoggerFactory.getLogger(DataReplicationService.class);

    @ConfigProperty(name = "api_key")
    private String apiKey;

    @ConfigProperty(name = "api_secret")
    private String apiSecret;

    @ConfigProperty(name = "braineous_url")
    private String braineousUrl;

    public void ingest(
            int streamSizeInObjects,
            JsonArray datasetElement,
            JsonArray replicationPipeline
    ){
        DataPlatformService dataPlatformService = DataPlatformService.getInstance();

        //configure the data pipeline client
        Configuration configuration = new Configuration().
                ingestionHostUrl(this.braineousUrl).
                apiKey(this.apiKey).
                apiSecret(this.apiSecret).
                streamSizeInObjects(streamSizeInObjects);
        dataPlatformService.configure(configuration);

        //register pipes
        this.registerReplicationPipeLine(dataPlatformService, replicationPipeline);

        //send source data through the replication pipeline (collection of pipes)
        this.sendData(
                dataPlatformService,
                replicationPipeline,
                datasetElement
        );
    }

    //------------------------------------------------------------------------------------
    private void registerReplicationPipeLine(DataPlatformService dataPlatformService,
                                             JsonArray pipesJsonArray){
        try {
            for (int i = 0; i < pipesJsonArray.size(); i++) {
                JsonObject configJson = pipesJsonArray.get(i).getAsJsonObject();
                dataPlatformService.registerPipe(configJson);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void sendData(DataPlatformService dataPlatformService,
                          JsonArray pipesJsonArray,
                          JsonElement datasetElement
    ){
        //make it parallel and in batches
        List<JsonObject> pipeConfigs = new ArrayList<>();
        for(int i=0; i<pipesJsonArray.size();i++) {
            JsonObject configJson = pipesJsonArray.get(i).getAsJsonObject();
            pipeConfigs.add(configJson);
        }

        //process pipes in parallel
        pipeConfigs.parallelStream().forEach(configJson -> {
            String pipeId = configJson.get("pipeId").getAsString();
            String entity = configJson.get("entity").getAsString();
            dataPlatformService.sendData(pipeId, entity, datasetElement.toString());
        });
    }
}
