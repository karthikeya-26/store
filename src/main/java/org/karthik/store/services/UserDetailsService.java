package org.karthik.store.services;

import org.karthik.store.cache.SessionCacheManager;
import org.karthik.store.cache.UserDetailsCacheManager;
import org.karthik.store.dao.SessionsDao;
import org.karthik.store.dao.UserDetailsDao;
import org.karthik.store.exceptions.BadRequestException;
import org.karthik.store.models.Sessions;
import org.karthik.store.models.UserDetails;

import java.util.List;
import java.util.UUID;

public class UserDetailsService {
    private final UserDetailsDao userDetailsDao = new UserDetailsDao();

    public List<UserDetails> getAllUserDetails() {
        return userDetailsDao.getUserDetails();
    }

    public UserDetails getUserDetails(String username) {
        return userDetailsDao.getUserDetails(username);
    }

    public Sessions authenticate(String userName, String password) {
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
        new SessionsDao().addSession(session);
        SessionCacheManager.addSession(session);
        UserDetailsCacheManager.addUserDetails(userDetails);
        return session;
    }

    public UserDetails addUserDetails(UserDetails userDetails) {
        if(userDetails.getUserName() == null || userDetails.getUserName().trim().isEmpty() || userDetails.getPassword() == null || userDetails.getPassword().isEmpty() || userDetails.getEmail() == null || userDetails.getEmail().isEmpty() || userDetails.getLocation() == null || userDetails.getLocation().isEmpty()) {
            throw new BadRequestException("User details are missing. Please provide all the details");
        }
        userDetails.setCreatedAt(System.currentTimeMillis());
        userDetailsDao.addUserDetails(userDetails);
        return userDetails;
    }

    public UserDetails updateUserDetails(UserDetails userDetails) {
        if(userDetails.getUserName() == null || userDetails.getUserName().isEmpty()) {
            return null;
        }
        userDetailsDao.updateUserDetails(userDetails);
        return userDetails;
    }

    public void removeUserDetails(String userName) {
        userDetailsDao.deleteUserDetails(userName);
    }

}
