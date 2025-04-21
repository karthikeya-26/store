package org.karthik.store.dao;

import com.adventnet.ds.query.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.WritableDataObject;
import org.karthik.store.dbutil.Database;
import org.karthik.store.exceptions.InternalServerErrorExcetion;
import org.karthik.store.models.Sessions;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SessionsDao {

    private static final Logger LOGGER = Logger.getLogger(SessionsDao.class.getName());

    public static Sessions getSession(String sessionId){
        Sessions session = null;
        Table table = new Table("Sessions");
        SelectQuery selectQuery = new SelectQueryImpl(table);
        Column column = new Column("Sessions", "*");
        selectQuery.addSelectColumn(column);
        Criteria criteria = new Criteria(new Column("Sessions","SESSION_ID"),sessionId,QueryConstants.EQUAL);
        selectQuery.setCriteria(criteria);
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = persistence.get(selectQuery);
            Iterator iterator = dataObject.getRows("Sessions");
            while(iterator.hasNext()){
                session = new Sessions();
                Row row = (Row) iterator.next();
                session.setSessionId(row.getString("SESSION_ID"));
                session.setUserId(row.getInt("USER_ID"));
                session.setCreatedAt(row.getLong("CREATED_AT"));
                session.setLastAccessedAt(row.getLong("LAST_ACCESSED_AT"));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return  session;
    }

    public static void addSession(Sessions session){
        try{
            DataObject dataObject = new WritableDataObject();
            Row row = new Row("Sessions");
            row.set("SESSION_ID", session.getSessionId());
            row.set("USER_ID", session.getUserId());
            row.set("CREATED_AT", session.getCreatedAt());
            row.set("LAST_ACCESSED_AT", session.getLastAccessedAt());
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            dataObject.addRow(row);
            persistence.add(dataObject);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion("Could not add session");
        }
    }

    public static void updateSession(Sessions session){
        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            UpdateQuery updateQuery = new UpdateQueryImpl("Sessions");
            updateQuery.setUpdateColumn("LAST_ACCESSED_AT",session.getLastAccessedAt());
            Criteria criteria = new Criteria(new Column("Sessions","SESSION_ID"),session.getSessionId(),QueryConstants.EQUAL);
            updateQuery.setCriteria(criteria);
            int udpatedRows = persistence.update(updateQuery);
        }catch(Exception e){
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            throw new InternalServerErrorExcetion("Could not update session");
        }
    }

    public static void deleteSession(String sessionId){
       try{
           Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
           DeleteQuery deleteQuery = new DeleteQueryImpl("Sessions");
           Column column = new Column("Sessions", "SESSION_ID");
           Criteria criteria = new Criteria(column,sessionId,QueryConstants.EQUAL);
           deleteQuery.setCriteria(criteria);
           int udpatedRows = persistence.delete(deleteQuery);
       } catch (Exception e) {
           LOGGER.log(Level.WARNING, e.getMessage(), e);
           throw new InternalServerErrorExcetion("Could not delete session");
       }
    }


}
