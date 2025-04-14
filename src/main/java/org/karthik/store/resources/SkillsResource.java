package org.karthik.store.resources;

import org.karthik.store.dao.UserDetailsDao;
import org.karthik.store.exceptions.DataNotFoundException;
import org.karthik.store.models.Skills;
import org.karthik.store.services.SkillsService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class SkillsResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkills(@PathParam("userName") String userName) {
         List<Skills> skills =SkillsService.getSkillsByUserName(userName);
         return Response.ok(skills).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSkills(@PathParam("userName")String userName, Skills skills) {
        skills.setUserId(UserDetailsDao.getUserDetails(userName).getUserId());
        Skills addedSkill= SkillsService.addSkill(skills);
        if(addedSkill!=null){
            return Response.status(Response.Status.CREATED).entity(addedSkill).build();
        }else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/{skillId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Skills getSkillsWithSkillName(@PathParam("userName") String userName,@PathParam("skillId") int skillId) {
        return SkillsService.getSkillBySkillIdAndUserName(userName,skillId);
    }

    @PUT
    @Path("/{skillId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSkills(@PathParam("userName")String userName,@PathParam("skillId") long skillId, Skills skills) {
        if(SkillsService.getSkillBySkillIdAndUserName(userName,skillId)==null){
            throw new DataNotFoundException("Skill does not exist for user "+userName);
        }
        skills.setSkillId(skillId);
        skills.setUserId(UserDetailsDao.getUserDetails(userName).getUserId());
        Skills updatedSkill =SkillsService.updateSkill(skills);
        return Response.status(Response.Status.ACCEPTED).entity(updatedSkill).build();
    }

    @DELETE
    @Path("/{skillId}")
    public Response deleteSkills(@PathParam("userName") String userName, @PathParam("skillId") int skillId) {
        if(SkillsService.getSkillBySkillIdAndUserName(userName,skillId)==null){
            throw new DataNotFoundException("Skill does not exist for user "+userName);
        }
        System.out.println("in deleteSkills");
        SkillsService.deleteSkill(skillId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
