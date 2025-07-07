package com.appgallabs.util;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class GenericResponseUtil {
    private static Logger logger = LoggerFactory.getLogger(GenericResponseUtil.class);

    public static Response internalServerError(){
        JsonObject message = new JsonObject();
        message.addProperty("message", "INTERNAL_SERVER_ERROR");
        return Response.serverError().entity(message.toString()).build();
    }
}
