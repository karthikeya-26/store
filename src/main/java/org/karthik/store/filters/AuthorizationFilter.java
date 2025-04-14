package org.karthik.store.filters;


import org.karthik.store.cache.SessionCacheManager;
import org.karthik.store.cache.UserDetailsCacheManager;
import org.karthik.store.models.ErrorMessage;
import org.karthik.store.models.Sessions;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

//@Provider
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext)  {
        if (isPublicUrl(requestContext.getUriInfo().getPath())) {
            return;
        }
        boolean isAuthorized  = checkAuthorization(requestContext);
        Sessions session = SessionCacheManager.getSession(requestContext.getCookies().get("session_id").getValue());
        if(!isAuthorized){
            LOGGER.info(String.format("User_Id %s, Session_Id %s, URI %s, RequestMethod %s -> Not Authorized", session.getSessionId(),session.getUserId(),requestContext.getUriInfo().getPath(),requestContext.getMethod()));
            handleUnAuthorizedRequest(requestContext);
            return;
        }
        LOGGER.info(String.format("User_Id %s, Session_Id %s, URI %s, RequestMethod %s -> Authorized", session.getSessionId(),session.getUserId(),requestContext.getUriInfo().getPath(),requestContext.getMethod()));
    }

    private boolean isPublicUrl(String requestUri) {
        return requestUri.matches("^(login|api/login|public/).*");
    }

    private boolean checkAuthorization(ContainerRequestContext requestContext) {
        Sessions session = SessionCacheManager.getSession(requestContext.getCookies().get("session_id").getValue());
        UriInfo uriInfo = requestContext.getUriInfo();
        if(!requestContext.getMethod().equals("GET")){
            return uriInfo.getPathParameters().get("userName").get(0).equals(UserDetailsCacheManager.getUserDetails(session.getUserId()).getUserName());
        }
        return true;
    }

    private void handleUnAuthorizedRequest(ContainerRequestContext requestContext) {
        ErrorMessage errorMessage = new ErrorMessage("You are not allowed to perform this action", 403);
        Response response = Response.status(Response.Status.FORBIDDEN).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
        requestContext.abortWith(response);
    }
}
