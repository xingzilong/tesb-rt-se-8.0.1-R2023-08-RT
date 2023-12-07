package org.talend.esb.sam.service.rest.ac;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ac")
public interface ACRestService {

    @POST
    @Path("ip/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addACIP(String requestBody);

    @DELETE
    @Path("ip/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response removeACIP(String requestBody);

    @PUT
    @Path("ip/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateACIP(String requestBody);

    @GET
    @Path("ip/listAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response listAllACIP();

    @GET
    @Path("ip/getACIP")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getACIP(@QueryParam("serviceKey") String serviceKey);

    @PUT
    @Path("ip/enable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response enableACIP(String requestBody);

    @PUT
    @Path("ip/disabled")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response disabledACIP(String requestBody);


    @POST
    @Path("flow/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addACFlow(String requestBody);

    @DELETE
    @Path("flow/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response removeACFlow(String requestBody);

    @PUT
    @Path("flow/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateACFlow(String requestBody);

    @GET
    @Path("flow/listAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response listAllACFlow();

    @GET
    @Path("flow/getACFlow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getACFlow(@QueryParam("serviceKey") String serviceKey);

    @PUT
    @Path("flow/enable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response enableACFlow(String requestBody);

    @PUT
    @Path("flow/disabled")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response disabledACFlow(String requestBody);


    @POST
    @Path("frequency/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addACFrequency(String requestBody);

    @DELETE
    @Path("frequency/remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response removeACFrequency(String requestBody);

    @PUT
    @Path("frequency/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateACFrequency(String requestBody);

    @GET
    @Path("frequency/listAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response listAllACFrequency();

    @GET
    @Path("flow/getACFrequency")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getACFrequency(@QueryParam("serviceKey") String serviceKey);

    @PUT
    @Path("frequency/enable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response enableACFrequency(String requestBody);

    @PUT
    @Path("frequency/disabled")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response disabledACFrequency(String requestBody);


    @GET
    @Path("record/listAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response listAllRecords();

    @GET
    @Path("rule/listAll")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response listAllRules();


}
