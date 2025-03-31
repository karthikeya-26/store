package org.karthik.store.resources;

import org.karthik.store.exceptions.AlreadyExistsException;
import org.karthik.store.models.UserDetails;
import org.karthik.store.services.UserDetailsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {

    private final UserDetailsService userDetailsService = new UserDetailsService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUserDetails(UserDetails userDetails) {
        if(userDetailsService.getUserDetails(userDetails.getUserName())!= null){
            throw new AlreadyExistsException("UserName already exists, please try again with new username");
        }
        return Response.status(Response.Status.CREATED).entity(userDetailsService.addUserDetails(userDetails)).build();
    }
}
