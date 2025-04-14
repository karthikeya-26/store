package org.karthik.store.services;

import org.karthik.store.dao.SkillsDao;
import org.karthik.store.exceptions.BadRequestException;
import org.karthik.store.exceptions.DataNotFoundException;
import org.karthik.store.models.Skills;

import java.util.List;
import java.util.logging.Logger;

public class SkillsService {
    private static final Logger LOGGER = Logger.getLogger(SkillsService.class.toString());

    public static List<Skills> getSkillBySkillName(String userName, String skillName) {
        if((skillName==null || skillName.isEmpty())&&(userName==null || userName.isEmpty())){
            throw new IllegalArgumentException("Skill name and user name cannot be null or empty");
        }
        List<Skills> skills = SkillsDao.getSkillBySkillName(userName,skillName);
        if(skills==null || skills.isEmpty()){
            LOGGER.info("No skills found for user "+userName+" and skill name "+skillName);
            throw new DataNotFoundException("Skill with name "+skillName+" not found for user "+userName+".");
        }
        return skills;

    }


    public static Skills getSkillBySkillIdAndUserName(String userName, long skillId) {
        if(skillId<=0 ){
            LOGGER.info("Invalid skill id "+skillId);
            throw new IllegalArgumentException("Skill id must be greater than 0");
        }
        Skills skill =  SkillsDao.getSkillBySkillId(userName,skillId);
        if(skill==null){
            throw new DataNotFoundException("Skill with id "+skillId+" for user "+userName+"  not found");
        }
        return skill;
    }

    public static List<Skills> getSkillsByUserName(String userName) {
        if(userName==null || userName.isEmpty()){
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        List<Skills> skills = SkillsDao.getUserSkills(userName);
        if(skills==null || skills.isEmpty()){
            throw new DataNotFoundException("Skills for user "+userName+" not found");
        }
        return skills;
    }
    public static Skills addSkill(Skills skills) {

        if(skills.getSkillName()==null || skills.getSkillName().isEmpty()|| skills.getSkillDescription()==null || skills.getSkillDescription().isEmpty()|| skills.getPricePerHour()==null|| skills.getPricePerHour().doubleValue()<=0){
            throw new BadRequestException(
                    "Skill name, skill description and price must be provided and valid");
        }
        skills.setCreatedAt(System.currentTimeMillis());
        return SkillsDao.addSkill(skills);
    }

    public static Skills updateSkill(Skills skills) {
        if(skills.getSkillName()==null || skills.getSkillName().isEmpty()|| skills.getSkillDescription()==null || skills.getSkillDescription().isEmpty()|| skills.getPricePerHour()==null|| skills.getPricePerHour().doubleValue()<=0){
            throw new BadRequestException(
                    "Skill name, skill description and price must be provided and valid");
        }
        skills.setUpdatedAt(System.currentTimeMillis());
        SkillsDao.updateSkill(skills);
        return skills;
    }

    public static void deleteSkill(int skillId) {
        SkillsDao.deleteSkill(skillId);
    }
}
