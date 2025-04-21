package org.karthik.store.resources;

import org.karthik.store.dao.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("public/hello")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() {
        Test.SelectDetails();
        return "<h1>Hello World!</h1>";
    }

}
