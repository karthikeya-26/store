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

@Provider
public class AuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)  {
        System.out.println("AuthorizationFilter");
        if (isPublicUrl(requestContext.getUriInfo().getPath())) {
            return;
        }
        boolean isAuthorized  = checkAuthorization(requestContext);
        System.out.println();
        System.out.println("Is Authorized : " + isAuthorized);
        if(!isAuthorized){
            handleUnAuthorizedRequest(requestContext);
        }
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
