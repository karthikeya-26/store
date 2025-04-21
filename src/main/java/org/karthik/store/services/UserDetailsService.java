package org.karthik.store.services;

import org.karthik.store.cache.SessionCacheManager;
import org.karthik.store.cache.UserDetailsCacheManager;
import org.karthik.store.dao.SessionsDao;
import org.karthik.store.dao.UserDetailsDao;
import org.karthik.store.exceptions.BadRequestException;
import org.karthik.store.models.Sessions;
import org.karthik.store.models.UserDetails;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class UserDetailsService {
    private  static final Logger LOGGER = Logger.getLogger(UserDetailsService.class.getName());
    public static List<UserDetails> getAllUserDetails() {
        return UserDetailsDao.getUserDetails();
    }

    public static UserDetails getUserDetails(String username) {
        UserDetails userDetails = UserDetailsDao.getUserDetails(username);

        if(userDetails != null){
            try{
                PrintWriter pw = new PrintWriter(new FileWriter("/home/karthi-pt7680/useremail.txt"));
                UserDetails user= UserDetailsDao.getUserDetailsWithEmail(userDetails.getEmail());
                LOGGER.info(user.toString());
                pw.println(user.toString());
                pw.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return userDetails;
    }

    public static Sessions authenticate(String userName, String password) {
        UserDetails userDetails = getUserDetails(userName);
        if(userDetails == null) {
            return null;
        }
        boolean validCredentials = userDetails.getPassword().equals(password);
        if(!validCredentials) {
            return null;
        }
        Sessions session = new Sessions();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(userDetails.getUserId());
        session.setCreatedAt(System.currentTimeMillis());
        session.setLastAccessedAt(System.currentTimeMillis());
        SessionsDao.addSession(session);
        SessionCacheManager.addSession(session);
        UserDetailsCacheManager.addUserDetails(userDetails);
        return session;
    }

    public static UserDetails addUserDetails(UserDetails userDetails) {
        if(userDetails.getUserName() == null || userDetails.getUserName().trim().isEmpty() || userDetails.getPassword() == null || userDetails.getPassword().isEmpty() || userDetails.getEmail() == null || userDetails.getEmail().isEmpty() || userDetails.getLocation() == null || userDetails.getLocation().isEmpty()) {
            throw new BadRequestException("User details are missing. Please provide all the details");
        }
        userDetails.setCreatedAt(System.currentTimeMillis());
        return UserDetailsDao.addUserDetails(userDetails);
    }

    public static UserDetails updateUserDetails(UserDetails userDetails) {
        if(userDetails.getUserName() == null || userDetails.getUserName().isEmpty()) {
            return null;
        }
        userDetails.setUpdatedAt(System.currentTimeMillis());
        return UserDetailsDao.updateUserDetails(userDetails);
    }

    public static void removeUserDetails(String userName) {
        UserDetailsDao.deleteUserDetails(userName);
    }

}
