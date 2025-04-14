package org.karthik.store.resources;


import org.karthik.store.exceptions.DataNotFoundException;
import org.karthik.store.exceptions.InternalServerErrorExcetion;
import org.karthik.store.models.UserDetails;
import org.karthik.store.services.UserDetailsService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Path("/users")
@Singleton
public class UserDetailsResource {
    private int counter = 0;
    public UserDetailsResource() {
        System.out.println(" creating user resource object counter = " + counter++ + "");
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDetails> getUserDetails() {
        List<UserDetails> users;
        users =  UserDetailsService.getAllUserDetails();
        return users;
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createUserDetails(UserDetails userDetails) {
//        if(userDetailsService.getUserDetails(userDetails.getUserName())!= null){
//            throw new AlreadyExistsException("UserName already exists, please try again with new username");
//        }
//        return Response.status(Response.Status.CREATED).entity(userDetailsService.addUserDetails(userDetails)).build();
//    }

    @GET
    @Path("/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDetails getUserDetails(@Context UriInfo uriInfo, @PathParam("userName") String userName) {
        UserDetails userDetails = UserDetailsService.getUserDetails(userName);
        if(userDetails == null){
            throw new DataNotFoundException("User does not exist");
        }
        String uri= uriInfo.getBaseUriBuilder().path(UserDetailsResource.class).path(userName).toString();
        userDetails.addLink("self", uri);
        return userDetails;
    }

    @PUT
    @Path("/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDetails updateUserDetails(@PathParam("userName") String userName, UserDetails userDetails) {
        userDetails.setUserName(userName);
        return UserDetailsService.updateUserDetails(userDetails);
    }

    @DELETE
    @Path("/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUserDetails(@PathParam("userName") String userName) {
        if(UserDetailsService.getUserDetails(userName)==null){
            throw new DataNotFoundException("User does not exist");
        }
        UserDetailsService.removeUserDetails(userName);
    }

    @Path("/{userName}/skills")
    public SkillsResource getSkillsResource(@PathParam("userName") String userName) {
        return new SkillsResource();
    }

}
