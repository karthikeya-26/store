package org.karthik.store.resources;

import org.karthik.store.cache.SessionCacheManager;
import org.karthik.store.dao.SessionsDao;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/logout")
public class LogoutResource {
    @Inject
    private SessionsDao sessionsDao;
    @Inject
    ContainerRequestContext requestContext;
    @POST
    public Response logout() {
        String sessionId = requestContext.getCookies().get("session_id").getValue();
        SessionCacheManager.removeSession(sessionId);
        sessionsDao.deleteSession(sessionId);
       return  Response.accepted().cookie(NewCookie.valueOf("session_id=;Max-Age=0;Path=/store_war_exploded/api/;HttpOnly;SameSite=Strict")).build();
    }
}
