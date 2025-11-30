package com.skupina1.resource;

import com.skupina1.entity.RideEntity;
import com.skupina1.model.RideRequest;
import com.skupina1.service.RideService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ride")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideResource {

    RideService service = new RideService();

    @POST
    public Response createRide(RideEntity req){
        System.out.println("TEST");
        boolean attempt = service.createRide(req);

        if(!attempt)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        return Response.ok(req).build();
    }

    @GET
    @Path("/{id}")
    public Response getRideById(@PathParam("id") long id){
        RideEntity ride = service.findById(id);

        if(ride == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"No ride by specified ID\"}")
                    .build();

        return Response.ok(ride).build();
    }

    @PUT
    @Path("/{rideId}/complete")
    public Response complete(@PathParam("rideId") long rideId) {
        RideEntity r = service.completeRide(rideId);
        return Response.ok(r).build();
    }
}
