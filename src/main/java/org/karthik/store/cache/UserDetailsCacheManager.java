package org.karthik.store.cache;

import org.karthik.store.dao.UserDetailsDao;
import org.karthik.store.models.UserDetails;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDetailsCacheManager {

    private final static int MAX_CACHE_SIZE = 1000;
    private static final Map<Integer, UserDetails> userDetailsCache = new LinkedHashMap<Integer,UserDetails>(MAX_CACHE_SIZE, 0.75f, true){
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, UserDetails> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };
    private UserDetailsCacheManager(){};

    public static UserDetails getUserDetails(Integer userId){
        UserDetails userDetails = userDetailsCache.get(userId);
        if(userDetails == null){
            synchronized (userDetailsCache){
                userDetails = userDetailsCache.get(userId);
                if(userDetails == null){
                    userDetails = new UserDetailsDao().getUserDetails(userId);
                    if(userDetails != null){
                        userDetailsCache.put(userId, userDetails);
                    }
                }
            }
        }
        return userDetails;
    }

    public static void addUserDetails(UserDetails userDetails){
        synchronized (userDetailsCache){
            userDetailsCache.put(userDetails.getUserId(), userDetails);
        }
    }

}
