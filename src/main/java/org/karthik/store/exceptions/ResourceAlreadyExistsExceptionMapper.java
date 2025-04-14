package org.karthik.store.exceptions;

import org.karthik.store.models.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceAlreadyExistsExceptionMapper implements ExceptionMapper<ResourceAlreadyExistsException> {


    @Override
    public Response toResponse(ResourceAlreadyExistsException exception) {
        ErrorMessage errorMessage  = new ErrorMessage(exception.getMessage(), 409);
        return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
    }
}
