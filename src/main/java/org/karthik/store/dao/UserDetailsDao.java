package org.karthik.store.dao;

import com.adventnet.db.api.RelationalAPI;
import com.adventnet.ds.query.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;
import org.karthik.store.exceptions.InternalServerErrorExcetion;
import org.karthik.store.models.UserDetails;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        Criteria criteria = selectQuery.getCriteria();


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
        userDetails.setUserId(dataSet.getInt("user_id"));
        userDetails.setUserName(dataSet.getAsString("user_name"));
        userDetails.setPassword(dataSet.getAsString("password"));
        userDetails.setEmail(dataSet.getAsString("email"));
        userDetails.setLocation(dataSet.getAsString("location"));
        userDetails.setCreatedAt(dataSet.getAsLong("created_at"));
        userDetails.setUpdatedAt(dataSet.getAsLong("updated_at")==null?0L: dataSet.getLong("updated_at"));
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
            updateQuery.setUpdateColumn("USER_NAME",userDetails.getUserName());
            updateQuery.setUpdateColumn("PASSWORD",userDetails.getPassword());
            updateQuery.setUpdateColumn("EMAIL",userDetails.getEmail());
            updateQuery.setUpdateColumn("LOCATION",userDetails.getLocation());
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
