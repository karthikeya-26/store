package org.karthik.store.dao;

import com.adventnet.ds.query.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;
import org.karthik.store.models.Skills;
import org.karthik.store.models.UserDetails;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillsDao {

    public static List<Skills> getUserSkills(String userName) {
        List<Skills> userSkillsList;
        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            Table table = new Table("Skills");
            SelectQuery selectQuery = new SelectQueryImpl(table);
            Column column = new Column("Skills", "*");
            Join join = new Join("Skills", "UserDetails", new String[]{"USER_ID"}, new String[]{"USER_ID"}, Join.INNER_JOIN);
            Criteria criteria = new Criteria(new Column("UserDetails","USER_NAME"),userName,QueryConstants.EQUAL);
            selectQuery.addSelectColumn(column);
            selectQuery.addJoin(join);
            selectQuery.setCriteria(criteria);
            DataObject dataObject = persistence.get(selectQuery);
            Iterator iterator = dataObject.getRows("Skills");
            userSkillsList = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = (Row) iterator.next();
                Skills skill = new Skills();
                skill.setSkillId(row.getLong("SKILL_ID"));
                skill.setSkillName(row.getString("SKILL_NAME"));
                skill.setSkillDescription(row.getString("SKILL_DESCRIPTION"));
                skill.setPricePerHour(row.getBigDecimal("PRICE_PER_HOUR"));
                skill.setUserId(row.getInt("USER_ID"));
                skill.setCreatedAt(row.getLong("CREATED_AT"));
                skill.setUpdatedAt(row.getLong("UPDATED_AT")!=null?row.getLong("UPDATED_AT"):null);
                userSkillsList.add(skill);
            }
        } catch (Exception e) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("/home/karthi-pt7680/Desktop/exception_trace.txt"))) {
                e.printStackTrace(writer);
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Log the failure to write the file
            }
            throw new RuntimeException(e);
        }
        return userSkillsList;
    }

    public static Skills getSkillBySkillId(long skillId) {
        Skills skills = null;
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            Table table = new Table("Skills");
            SelectQuery selectQuery = new SelectQueryImpl(table);
            Column column = new Column("Skills", "*");
            selectQuery.addSelectColumn(column);
            Criteria criteria = new Criteria(new Column("Skills","SKILL_ID"),skillId,QueryConstants.EQUAL);

            selectQuery.setCriteria(criteria);
            DataObject dataObject = persistence.get(selectQuery);
            Iterator iterator = dataObject.getRows("Skills");
            while (iterator.hasNext()) {
                Row row = (Row) iterator.next();
                skills = new Skills();
                skills.setSkillId(row.getLong("SKILL_ID"));
                skills.setSkillName(row.getString("SKILL_NAME"));
                skills.setSkillDescription(row.getString("SKILL_DESCRIPTION"));
                skills.setPricePerHour(row.getBigDecimal("PRICE_PER_HOUR"));
                skills.setUserId(row.getInt("USER_ID"));
                skills.setCreatedAt(row.getLong("CREATED_AT"));
                skills.setUpdatedAt(row.getLong("UPDATED_AT")!=null?row.getLong("UPDATED_AT"):null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return skills;
    }

    public static Skills getSkillBySkillId(String userName,long skillId) {
        Skills skill = null;
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            Table table = new Table("Skills");
            SelectQuery selectQuery = new SelectQueryImpl(table);
            Column column = new Column("Skills", "*");
            Join join = new Join("Skills", "UserDetails", new String[]{"USER_ID"}, new String[]{"USER_ID"}, Join.INNER_JOIN);
            Criteria skillIdCriteria = new Criteria(new Column("Skills", "SKILL_ID"),skillId,QueryConstants.EQUAL);
            Criteria userNameAndSkillIdCriteria = skillIdCriteria.and(new Criteria(new Column("UserDetails","USER_NAME"),userName,QueryConstants.EQUAL));
            selectQuery.addSelectColumn(column);
            selectQuery.addJoin(join);
            selectQuery.setCriteria(userNameAndSkillIdCriteria);
            DataObject dataObject = persistence.get(selectQuery);
            Iterator iterator = dataObject.getRows("Skills");
            while (iterator.hasNext()) {
                Row row = (Row) iterator.next();
                skill = new Skills();
                skill.setSkillId(row.getLong("SKILL_ID"));
                skill.setSkillName(row.getString("SKILL_NAME"));
                skill.setSkillDescription(row.getString("SKILL_DESCRIPTION"));
                skill.setPricePerHour(row.getBigDecimal("PRICE_PER_HOUR"));
                skill.setUserId(row.getInt("USER_ID"));
                skill.setCreatedAt(row.getLong("CREATED_AT"));
                skill.setUpdatedAt(row.getLong("UPDATED_AT")!=null?row.getLong("UPDATED_AT"):null);
            }
        } catch (Exception e) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("/home/karthi-pt7680/Desktop/exception_trace.txt"))) {
                e.printStackTrace(writer);
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Log the failure to write the file
            }
            throw new RuntimeException(e);
        }
        return skill;
    }

    public static List<Skills> getSkillBySkillName(String userName, String skillName) {

        List<Skills> skills = new ArrayList<>();
        Table table = new Table("Skills");
        SelectQuery selectQuery = new SelectQueryImpl(table);
        Column skillColumns = new Column("Skills", "*");
        selectQuery.addSelectColumn(skillColumns);
        UserDetails userDetails = UserDetailsDao.getUserDetails(userName);
        Criteria criteria = new Criteria(new Column[]{new Column("Skills","SKILL_NAME"),new Column("Skills","USER_ID")},new Object[]{skillName,userDetails.getUserId()},QueryConstants.EQUAL);
        selectQuery.setCriteria(criteria);
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = persistence.get(selectQuery);
            Iterator iterator = dataObject.getRows("Skills");
            while (iterator.hasNext()) {
                Row row = (Row) iterator.next();
                Skills skill = new Skills();
                skill.setSkillId(row.getLong("skill_id"));
                skill.setSkillName(row.getString("skill_name"));
                skill.setSkillDescription(row.getString("skill_description"));
                skill.setPricePerHour(row.getBigDecimal("price_per_hour"));
                skill.setUserId(row.getInt("user_id"));
                skill.setCreatedAt(row.getLong("created_at"));
                skill.setUpdatedAt(row.getLong("updated_at"));
                skills.add(skill);
            }

        } catch (Exception e) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("/home/karthi-pt7680/Desktop/exception_trace.txt"))) {
                e.printStackTrace(writer);
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Log the failure to write the file
            }
            throw new RuntimeException(e);
        }
        return skills;
    }
    private static void prepareSkills(List<Skills> skillsList, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Skills skills = new Skills();
            skills.setSkillId(resultSet.getLong("skill_id"));
            skills.setSkillName(resultSet.getString("name"));
            skills.setSkillDescription(resultSet.getString("description"));
            skills.setUserId(resultSet.getInt("user_id"));
            skills.setPricePerHour(resultSet.getBigDecimal("price_per_hour"));
            skills.setCreatedAt(resultSet.getLong("created_at"));
            skills.setUpdatedAt(resultSet.getString("updated_at")!= null ? resultSet.getLong("updated_at"):null);
            skillsList.add(skills);
        }
    }

    public static Skills addSkill(Skills skills) {
        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = new WritableDataObject();
            Row row = new Row("Skills");
            row.set("SKILL_NAME", skills.getSkillName());
            row.set("SKILL_DESCRIPTION", skills.getSkillDescription());
            row.set("PRICE_PER_HOUR", skills.getPricePerHour());
            row.set("USER_ID", skills.getUserId());
            row.set("CREATED_AT", skills.getCreatedAt());
            dataObject.addRow(row);
            DataAccess.add(dataObject);
        }catch (Exception e){
            try (PrintWriter writer = new PrintWriter(new FileWriter("/home/karthi-pt7680/Desktop/exception_trace.txt"))) {
                e.printStackTrace(writer);
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Log the failure to write the file
            }
            throw new RuntimeException(e);
        }

        return skills;
    }

    public static Skills updateSkill(Skills skill) {
        Skills oldSkill = getSkillBySkillId(skill.getSkillId());
        skill.setCreatedAt(oldSkill.getCreatedAt());
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            UpdateQuery updateQuery = new UpdateQueryImpl("Skills");
            Criteria criteria = new Criteria(new Column("Skills","SKILL_ID"),skill.getSkillId(),QueryConstants.EQUAL);
            updateQuery.setCriteria(criteria);
            updateQuery.setUpdateColumn("SKILL_NAME",skill.getSkillName());
            updateQuery.setUpdateColumn("SKILL_DESCRIPTION",skill.getSkillDescription());
            updateQuery.setUpdateColumn("PRICE_PER_HOUR",skill.getPricePerHour());
            updateQuery.setUpdateColumn("UPDATED_AT",skill.getUpdatedAt());
            int updated = persistence.update(updateQuery);
            DataAccess.update(updateQuery);
            if(updated <=0){
                throw new RuntimeException("Update failed");
            }
        } catch (Exception e) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("/home/karthi-pt7680/Desktop/exception_trace.txt"))) {
                e.printStackTrace(writer);
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Log the failure to write the file
            }
            throw new RuntimeException(e);
        }
        return skill;
    }

    public static void deleteSkill(int skillId) {
        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DeleteQuery deleteQuery = new DeleteQueryImpl("Skills");
            Criteria criteria = new Criteria(new Column("Skills","SKILL_ID"),skillId,QueryConstants.EQUAL);
            deleteQuery.setCriteria(criteria);
            int deleted = persistence.delete(deleteQuery);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
