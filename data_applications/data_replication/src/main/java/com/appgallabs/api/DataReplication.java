package com.appgallabs.api;

import com.appgallabs.service.DataReplicationService;
import com.google.gson.JsonObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ingest")
public class DataReplication {

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

        //response
        JsonObject message = new JsonObject();
        message.addProperty("message", "DATA_INGESTION_SUCCESS");

        Response response = Response.ok(message.toString()).build();
        return response;
    }
}
