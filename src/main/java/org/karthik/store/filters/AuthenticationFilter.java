package org.karthik.store.filters;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.karthik.store.cache.SessionCacheManager;
import org.karthik.store.models.ErrorMessage;
import org.karthik.store.models.Link;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import org.karthik.store.models.Sessions;
@PreMatching
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (isPublicUrl(requestContext.getUriInfo().getPath())) {
            return;
        }
        boolean isAuthenticated = checkAuthentication(requestContext);
        System.out.println("Is Authenticated : " + isAuthenticated);
        if (!isAuthenticated) {
            handleUnAuthenticatedRequest(requestContext);
            return;
        }
        Sessions session = SessionCacheManager.getSession(requestContext.getCookies().get("session_id").getValue());
        requestContext.setProperty("session_id", session.getSessionId());
        requestContext.setProperty("user_id", session.getUserId());
    }


    private boolean isPublicUrl(String requestUri) {
        return requestUri.matches("^(login|api/login|public/).*");
    }


    private boolean checkAuthentication(ContainerRequestContext requestContext) {
        boolean hasSessionId = (requestContext.getCookies().get("session_id") != null) ? !requestContext.getCookies().get("session_id").getValue().isEmpty() : false;
        if(!hasSessionId) {
            return false;
        }
        Sessions session = SessionCacheManager.getSession(requestContext.getCookies().get("session_id").getValue());
        return session != null;
    }

    private boolean checkAuthorization(String sessionId, ContainerRequestContext requestContext) {
        System.out.println("Checking authorization for session id : " + sessionId);

        return true;
    }

    private void handleUnAuthenticatedRequest(ContainerRequestContext requestContext) {
        UriInfo uriInfo = requestContext.getUriInfo();
        String loginUrl = uriInfo.getBaseUriBuilder().path("login").build().toString();
        Link notAuthenticatedMessage = new Link();
        notAuthenticatedMessage.setUrl(loginUrl);
        notAuthenticatedMessage.setRel("login");
        Response response = Response.status(Response.Status.UNAUTHORIZED).entity(notAuthenticatedMessage).build();
        requestContext.abortWith(response);
    }

    private void handleUnAuthorizedRequest(ContainerRequestContext requestContext) {
        UriInfo uriInfo = requestContext.getUriInfo();
        //log the action
        ErrorMessage errorMessage = new ErrorMessage("You are not allowed to perform this action", 403);
        Response response = Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
        requestContext.abortWith(response);
    }

}
