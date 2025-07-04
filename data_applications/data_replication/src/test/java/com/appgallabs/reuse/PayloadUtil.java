package com.appgallabs.reuse;

import com.appgallabs.dataplatform.util.JsonUtil;
import com.appgallabs.dataplatform.util.Util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadUtil {
    private static Logger logger = LoggerFactory.getLogger(PayloadUtil.class);

    public static JsonArray discoverReplicationPipeLine(){
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

    public static JsonElement dataset() throws Exception{
        String datasetLocation = "dataset/data.json";
        String json = Util.loadResource(datasetLocation);
        JsonElement datasetElement = JsonUtil.validateJson(json);
        logger.info("*****DATA_SET******");
        JsonUtil.printStdOut(datasetElement);

        return datasetElement;
    }
}
