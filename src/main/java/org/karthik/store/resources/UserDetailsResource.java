package org.karthik.store.resources;


import org.karthik.store.exceptions.AlreadyExistsException;
import org.karthik.store.exceptions.DataNotFoundException;
import org.karthik.store.models.UserDetails;
import org.karthik.store.services.UserDetailsService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/users")
public class UserDetailsResource {

    private final UserDetailsService userDetailsService = new UserDetailsService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDetails> getUserDetails() {
        return userDetailsService.getAllUserDetails();
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
        System.out.println("userName = " + userName);
        UserDetails userDetails = userDetailsService.getUserDetails(userName);
        if(userDetails == null){
            throw new DataNotFoundException("User does not exist");
        }
        String uri= uriInfo.getBaseUriBuilder().path(UserDetailsResource.class).path(userName).toString();
        System.out.println(uri);
        userDetails.addLink("self", uri);
        return userDetails;
    }

    @PUT
    @Path("/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDetails updateUserDetails(@PathParam("userName") String userName, UserDetails userDetails) {
        userDetails.setUserName(userName);
        return userDetailsService.updateUserDetails(userDetails);
    }

    @DELETE
    @Path("/{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteUserDetails(@PathParam("userName") String userName) {
        if(userDetailsService.getUserDetails(userName)==null){
            throw new DataNotFoundException("User does not exist");
        }
        userDetailsService.removeUserDetails(userName);
    }

    @Path("/{userName}/skills")
    public SkillsResource getSkillsResource(@PathParam("userName") String userName) {
        System.out.println("getSkillsResource "+userName);
        return new SkillsResource();
    }

}
