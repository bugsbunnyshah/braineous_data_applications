package com.appgallabs;

import com.appgallabs.dataplatform.client.sdk.api.Configuration;
import com.appgallabs.dataplatform.client.sdk.api.DataPlatformService;

import com.appgallabs.dataplatform.util.JsonUtil;
import com.appgallabs.dataplatform.util.Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DataReplicationClient {

    public static void main(String[] args) throws Exception{
        DataReplicationClient dataReplicationClient = new DataReplicationClient();
        DataPlatformService dataPlatformService = DataPlatformService.getInstance();

        String apiKey = "ffb2969c-5182-454f-9a0b-f3f2fb0ebf75";
        String apiSecret = "5960253b-6645-41bf-b520-eede5754196e";

        String datasetLocation = "dataset/data.json";
        String json = Util.loadResource(datasetLocation);
        JsonElement datasetElement = JsonUtil.validateJson(json);
        System.out.println("*****DATA_SET******");
        JsonUtil.printStdOut(datasetElement);

        //configure the DataPipeline Client
        Configuration configuration = new Configuration().
                ingestionHostUrl("http://localhost:8080/").
                apiKey(apiKey).
                apiSecret(apiSecret).
                streamSizeInObjects(0);
        dataPlatformService.configure(configuration);

        JsonArray replicationPipeline = dataReplicationClient.
                discoverReplicationPipeLine();

        //register pipes
        dataReplicationClient.registerReplicationPipeLine(dataPlatformService,
                replicationPipeline
        );

        //send source data through the replication pipeline (collection of pipes)
        dataReplicationClient.sendData(dataPlatformService,
                replicationPipeline,
                datasetElement
        );
    }

    private JsonArray discoverReplicationPipeLine(){
        try {
            JsonArray replicationPipes = new JsonArray();

            String configLocation = "pipe_config/pipe_config.json";
            String pipeConfigJson = Util.loadResource(configLocation);
            JsonArray pipesJsonArray = JsonUtil.validateJson(pipeConfigJson).getAsJsonArray();
            for(int i=0; i<pipesJsonArray.size(); i++) {
                JsonObject configJson = pipesJsonArray.get(i).getAsJsonObject();
                System.out.println("*****PIPE_CONFIGURATION******");
                JsonUtil.printStdOut(configJson);
                replicationPipes.add(configJson);
            }

            return replicationPipes;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void registerReplicationPipeLine(DataPlatformService dataPlatformService,
                                             JsonArray pipesJsonArray){
        try {
            for (int i = 0; i < pipesJsonArray.size(); i++) {
                JsonObject configJson = pipesJsonArray.get(i).getAsJsonObject();
                dataPlatformService.registerPipe(configJson);
                System.out.println("*****PIPE_REGISTRATION_SUCCESS******");
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void sendData(DataPlatformService dataPlatformService,
                          JsonArray pipesJsonArray,
                          JsonElement datasetElement
    ){
        //TODO: make it concurrent and in batches
        for(int i=0; i<pipesJsonArray.size();i++) {
            JsonObject configJson = pipesJsonArray.get(i).getAsJsonObject();
            String pipeId = configJson.get("pipeId").getAsString();
            String entity = configJson.get("entity").getAsString();
            dataPlatformService.sendData(pipeId, entity, datasetElement.toString());
        }
        System.out.println("*****DATA_INGESTION_SUCCESS******");
    }
}
