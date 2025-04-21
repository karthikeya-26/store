package org.karthik.store.resources;

import org.karthik.store.exceptions.NotAuthorizedException;
import org.karthik.store.models.LoginRequest;
import org.karthik.store.models.Sessions;
import org.karthik.store.services.UserDetailsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.logging.Logger;

@Path("/login")
public class LoginResource {

    private static final Logger LOGGER = Logger.getLogger(LoginResource.class.getName());
    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        Sessions session = UserDetailsService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if(session == null){
            throw new NotAuthorizedException("Invalid username or password");
        }
        NewCookie cookie = new NewCookie("session_id", session.getSessionId(),uriInfo.getBaseUri().getPath() , null, null, -1 , false, true);
        return Response.ok().cookie(cookie).build();
    }
}
