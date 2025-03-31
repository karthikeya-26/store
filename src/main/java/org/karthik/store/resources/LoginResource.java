package org.karthik.store.resources;

import org.karthik.store.cache.SessionCacheManager;
import org.karthik.store.cache.UserDetailsCacheManager;
import org.karthik.store.dao.UserDetailsDao;
import org.karthik.store.exceptions.NotAuthorizedException;
import org.karthik.store.models.LoginRequest;
import org.karthik.store.models.Sessions;
import org.karthik.store.models.UserDetails;
import org.karthik.store.services.UserDetailsService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginResource {

    private final UserDetailsService userDetailsService = new UserDetailsService();
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        Sessions session = userDetailsService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if(session == null){
            throw new NotAuthorizedException("Invalid username or password");
        }
        NewCookie cookie = new NewCookie("session_id", session.getSessionId(), "/store_war_exploded/api/", null, null, -1 , false, true);
        return Response.ok().cookie(cookie).build();
    }
}
