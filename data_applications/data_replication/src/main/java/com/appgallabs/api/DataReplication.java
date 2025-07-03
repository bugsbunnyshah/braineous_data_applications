package com.appgallabs.api;

import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ingest")
public class DataReplication {

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
