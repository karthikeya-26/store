package org.karthik.store.dao;

import com.adventnet.db.api.RelationalAPI;
import com.adventnet.ds.query.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;
import org.karthik.store.exceptions.InternalServerErrorExcetion;
import org.karthik.store.models.UserDetails;

import javax.enterprise.inject.spi.Bean;
import javax.xml.crypto.Data;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDetailsDao {

    private static final Logger LOGGER = Logger.getLogger(UserDetailsDao.class.getName());

    public UserDetailsDao() {
    }
    public static List<UserDetails> getUserDetails() {
        Table table = new Table("UserDetails");
        SelectQuery selectQuery = new SelectQueryImpl(table);
        Column column = new Column("UserDetails","*");
        selectQuery.addSelectColumn(column);
        RelationalAPI relationalAPI = RelationalAPI.getInstance();
        List<UserDetails> userDetailsList = new ArrayList<>();
        try (Connection connection =relationalAPI.getConnection();
             DataSet dataSet = relationalAPI.executeQuery(selectQuery,connection);) {
            while (dataSet.next()) {
                UserDetails userDetails = new UserDetails();
                prepareUserDetails(userDetails, dataSet);
                userDetailsList.add(userDetails);
            }
        } catch (SQLException | QueryConstructionException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion("Error while getting user details");
        }
        return userDetailsList;
    }

    public static UserDetails getUserDetails(int userId) {
        UserDetails userDetails = null;
        Table table = new Table("UserDetails");
        SelectQuery selectQuery = new SelectQueryImpl(table);
        Column column = new Column("UserDetails","*");
        Criteria criteria = new Criteria(new Column("UserDetails","USER_ID"),userId,QueryConstants.EQUAL);
        selectQuery.addSelectColumn(column);
        selectQuery.setCriteria(criteria);
        RelationalAPI relationalAPI = RelationalAPI.getInstance();
        try (Connection connection = relationalAPI.getConnection();
            DataSet dataSet = relationalAPI.executeQuery(selectQuery,connection)){

            while (dataSet.next()) {
                userDetails = new UserDetails();
                prepareUserDetails(userDetails, dataSet);
            }
        } catch (SQLException | QueryConstructionException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return userDetails;
    }


    public static  UserDetails getUserDetailsWithEmail(String email) {
        UserDetails userDetails = null;
        Table table = new Table("UserDetails");
        SelectQuery selectQuery = new SelectQueryImpl(table);
        Column column = new Column("UserDetails","EMAIL");
        selectQuery.addSelectColumn(column);
        Column column2 = new Column("UserDetails","USER_ID");
        selectQuery.addSelectColumn(column2);
        Criteria criteria = new Criteria(new Column("UserDetails", "EMAIL"),email,QueryConstants.EQUAL);
        selectQuery.setCriteria(criteria);
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = persistence.get(selectQuery);
            Iterator iterator = dataObject.getRows("UserDetails");
            while (iterator.hasNext()) {
                Row row = (Row) iterator.next();
                userDetails = new UserDetails();
                userDetails.setEmail(row.getString("EMAIL"));
                userDetails.setUserName(row.getString("USER_NAME"));
                userDetails.setPassword(row.getString("PASSWORD"));
                userDetails.setUserId(row.getInt("USER_ID"));
                userDetails.setCreatedAt(row.getLong("CREATED_AT"));
                userDetails.setUpdatedAt(row.getLong("UPDATED_AT"));
                userDetails.setLocation(row.getString("LOCATION"));
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion(e);
        }

        return userDetails;
    }
    public static UserDetails getUserDetails(String userName) {
        UserDetails userDetails = null;
        Table table = new Table("UserDetails");
        SelectQuery selectQuery = new SelectQueryImpl(table);
        Column column = new Column("UserDetails","*");
        Criteria criteria = new Criteria(new Column("UserDetails","USER_NAME"),userName,QueryConstants.EQUAL);
        selectQuery.addSelectColumn(column);
        selectQuery.setCriteria(criteria);
        try{
            PrintWriter printWriter = new PrintWriter(new FileWriter("/home/karthi-pt7680/Desktop/query.txt"));
            printWriter.println(selectQuery);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RelationalAPI relationalAPI = RelationalAPI.getInstance();
        try (Connection connection = relationalAPI.getConnection();
             DataSet dataSet = relationalAPI.executeQuery(selectQuery,connection)){
            while (dataSet.next()) {
                userDetails = new UserDetails();
                prepareUserDetails(userDetails, dataSet);
            }
        } catch (SQLException | QueryConstructionException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return userDetails;
    }

    private static void prepareUserDetails(UserDetails userDetails, DataSet dataSet) throws SQLException {
//        userDetails.setUserId(dataSet.getInt("user_id"));
//        userDetails.setUserName(dataSet.getAsString("user_name"));
//        userDetails.setPassword(dataSet.getAsString("password"));
//        userDetails.setEmail(dataSet.getAsString("email"));
//        userDetails.setLocation(dataSet.getAsString("location"));
//        userDetails.setCreatedAt(dataSet.getAsLong("created_at"));
//        userDetails.setUpdatedAt(dataSet.getAsLong("updated_at")==null?0L: dataSet.getLong("updated_at"));
        Integer userId = dataSet.getInt("user_id");
        if (userId != null) {
            userDetails.setUserId(userId);
        }

        // Set user_name with null check
        String userName = dataSet.getAsString("user_name");
        if (userName != null) {
            userDetails.setUserName(userName);
        }

        // Set password with null check
        String password = dataSet.getAsString("password");
        if (password != null) {
            userDetails.setPassword(password);
        }

        // Set email with null check
        String email = dataSet.getAsString("email");
        if (email != null) {
            userDetails.setEmail(email);
        }

        // Set location with null check
        String location = dataSet.getAsString("location");
        if (location != null) {
            userDetails.setLocation(location);
        }

        // Set created_at with null check
        Long createdAt = dataSet.getAsLong("created_at");
        if (createdAt != null) {
            userDetails.setCreatedAt(createdAt);
        }

        // Set updated_at with null check
        Long updatedAt = dataSet.getAsLong("updated_at");
        if (updatedAt != null) {
            userDetails.setUpdatedAt(updatedAt);
        } else {
            userDetails.setUpdatedAt(0L); // Default value if updated_at is null
        }
    }

    private static void prepareUserDetails(UserDetails userDetails, ResultSet resultSet) throws SQLException {
        userDetails.setUserId(resultSet.getInt("user_id"));
        userDetails.setUserName(resultSet.getString("user_name"));
        userDetails.setPassword(resultSet.getString("password"));
        userDetails.setEmail(resultSet.getString("email"));
        userDetails.setLocation(resultSet.getString("location"));
        userDetails.setCreatedAt(resultSet.getLong("created_at"));
        userDetails.setUpdatedAt(resultSet.getString("updated_at")==null?0L:resultSet.getLong("updated_at"));
    }

    public static UserDetails addUserDetails(UserDetails userDetails) {
        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = new WritableDataObject();
            Row row = new Row("UserDetails");
            row.set("USER_NAME", userDetails.getUserName());
            row.set("PASSWORD", userDetails.getPassword());
            row.set("EMAIL", userDetails.getEmail());
            row.set("LOCATION", userDetails.getLocation());
            row.set("CREATED_AT", userDetails.getCreatedAt());
            dataObject.addRow(row);
            persistence.add(dataObject);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion("Error while adding user details");
        }
        return userDetails;
    }

    public static UserDetails updateUserDetails(UserDetails userDetails)  {

        UserDetails oldUserDetails = getUserDetails(userDetails.getUserName());
        userDetails.setUserId(oldUserDetails.getUserId());
        userDetails.setCreatedAt(oldUserDetails.getCreatedAt());
        Logger.getAnonymousLogger().log(Level.INFO, "User Details Updated to " + userDetails.getUserId());
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            UpdateQuery updateQuery = new UpdateQueryImpl("UserDetails");
            Criteria criteria = new Criteria(new Column("UserDetails","USER_ID"),userDetails.getUserId(),QueryConstants.EQUAL);
            updateQuery.setCriteria(criteria);
            if(userDetails.getPassword()!=null){
                updateQuery.setUpdateColumn("PASSWORD",userDetails.getPassword());
            }
            if(userDetails.getEmail()!=null){
                updateQuery.setUpdateColumn("EMAIL",userDetails.getEmail());
            }
            if(userDetails.getLocation()!=null){
                updateQuery.setUpdateColumn("LOCATION",userDetails.getLocation());
            }
            updateQuery.setUpdateColumn("UPDATED_AT",userDetails.getUpdatedAt());
            int updated = persistence.update(updateQuery);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion("Error while updating user details");
        }
        return  userDetails;
    }

    public static void deleteUserDetails(String userName) {
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DeleteQuery deleteQuery = new DeleteQueryImpl("UserDetails");
            Column column = new Column("UserDetails","USER_NAME");
            Criteria criteria = new Criteria(column,userName,QueryConstants.EQUAL);
            deleteQuery.setCriteria(criteria);
            int deleted = persistence.delete(deleteQuery);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion("Error while deleting user details");
        }
    }
}
