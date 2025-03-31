package org.karthik.store.services;

import org.karthik.store.dao.SkillsDao;
import org.karthik.store.exceptions.BadRequestException;
import org.karthik.store.exceptions.DataNotFoundException;
import org.karthik.store.models.Skills;

import java.util.List;

public class SkillsService {
    private SkillsDao skillsDao = new SkillsDao();

    public List<Skills> getSkills() {
        return skillsDao.getAllSkills();
    }

    public List<Skills> getSkillBySkillName(String userName, String skillName) {
        if((skillName==null || skillName.isEmpty())&&(userName==null || userName.isEmpty())){
            throw new IllegalArgumentException("Skill name and user name cannot be null or empty");
        }
        List<Skills> skills = skillsDao.getSkillBySkillName(userName,skillName);
        if(skills==null || skills.isEmpty()){
            throw new DataNotFoundException("Skill with name "+skillName+" not found for user "+userName+".");
        }
        return skills;

    }

    public Skills getSkillBySkillId(String userName, int skillId) {
        if(skillId<=0 ){
            throw new IllegalArgumentException("Skill id must be greater than 0");
        }
        Skills skill =  skillsDao.getSkillBySkillId(userName,skillId);
        if(skill==null){
            throw new DataNotFoundException("Skill with id "+skillId+" for user "+userName+"  not found");
        }
        return skill;
    }

    public List<Skills> getSkillsByUserName(String userName) {
        if(userName==null || userName.isEmpty()){
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        List<Skills> skills = skillsDao.getUserSkills(userName);
        if(skills==null || skills.isEmpty()){
            throw new DataNotFoundException("Skills for user "+userName+" not found");
        }
        return skills;
    }
    public Skills addSkill(Skills skills) {

        if(skills.getSkillName()==null || skills.getSkillName().isEmpty()|| skills.getSkillDescription()==null || skills.getSkillDescription().isEmpty()|| skills.getPrice()==null|| skills.getPrice().doubleValue()<=0){
            throw new BadRequestException(
                    "Skill name, skill description and price must be provided and valid");
        }
        skills.setCreatedAt(System.currentTimeMillis());
        return skillsDao.addSkill(skills);
    }

    public void updateSkill(Skills skills) {
        if(skills.getSkillName()==null || skills.getSkillName().isEmpty()|| skills.getSkillDescription()==null || skills.getSkillDescription().isEmpty()|| skills.getPrice()==null|| skills.getPrice().doubleValue()<=0){
            throw new BadRequestException(
                    "Skill name, skill description and price must be provided and valid");
        }
        skills.setUpdatedAt(System.currentTimeMillis());
        skillsDao.updateSkill(skills);
    }

    public void deleteSkill(int skillId) {
        skillsDao.deleteSkill(skillId);
    }
}
