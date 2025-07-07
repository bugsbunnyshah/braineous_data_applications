package com.appgallabs.api;

import com.appgallabs.dataplatform.util.JsonUtil;
import com.appgallabs.service.DataReplicationService;
import com.appgallabs.util.GenericResponseUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ingest")
public class DataReplication {
    private static Logger logger = LoggerFactory.getLogger(DataReplication.class);

    @Inject
    private DataReplicationService dataReplicationService;

    public DataReplication() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping(){
        //response
        JsonObject message = new JsonObject();
        message.addProperty("message", "DATA_INGESTION_PING");

        Response response = Response.ok(message.toString()).build();
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response ingest(String data) {
        try {
            //Json Payload
            JsonObject message = new JsonObject();

            //input
            int streamSizeInObject;
            JsonArray datasetElement;
            JsonArray replicationPipeline;

            //validate input
            JsonObject payload = null;
            try {
                payload = JsonUtil.validateJson(data).getAsJsonObject();
                streamSizeInObject = payload.get("stream_size_in_objects").getAsInt();
                datasetElement = payload.get("dataset").getAsJsonArray();
                replicationPipeline = payload.get("replication_pipeline").getAsJsonArray();
            } catch (Exception e) {
                message.addProperty("message", "INVALID_INPUT_FORMAT");
                Response response = Response.status(422).entity(message.toString()).build();
                return response;
            }

            //process input
            this.dataReplicationService.ingest(
                    streamSizeInObject,
                    datasetElement,
                    replicationPipeline
            );

            //respond...got here..SUCCESS
            message.addProperty("message", "DATA_INGESTION_SUCCESS");
            return Response.ok(message.toString()).build();
        }catch(Exception e){
            return GenericResponseUtil.internalServerError();
        }
    }
}
