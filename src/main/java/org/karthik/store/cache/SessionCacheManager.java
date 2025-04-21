package org.karthik.store.cache;

import org.karthik.store.dao.SessionsDao;
import org.karthik.store.models.Sessions;

import java.util.LinkedHashMap;
import java.util.Map;

public class SessionCacheManager {

    private static final int MAX_CACHE_SIZE = 1000;

    private SessionCacheManager(){};
    private static final Map<String, Sessions> sessionCache = new LinkedHashMap<String,Sessions>(MAX_CACHE_SIZE, 0.75f, true){
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Sessions> eldest) {
            System.out.println(eldest.getKey()+"/"+eldest.getValue());
            System.out.println("Size of cache: "+size());
            if(size() > MAX_CACHE_SIZE){
                SessionsDao.updateSession(eldest.getValue());
            }
            return size() > MAX_CACHE_SIZE;
        }
    };

    public static Sessions getSession(String sessionId) {
        Sessions sessions = sessionCache.get(sessionId);
        if (sessions == null) {
            synchronized (sessionCache) {
                sessions = sessionCache.get(sessionId);
                if (sessions == null) {
                    sessions = SessionsDao.getSession(sessionId);
                    if (sessions != null) {
                        sessionCache.put(sessionId, sessions);
                    }
                }
            }
        }
        return sessions;
    }

    public static void removeSession(String sessionId){
        synchronized (sessionCache){
            sessionCache.remove(sessionId);
        }
    }

    public static void addSession(Sessions sessions){
        synchronized (sessionCache){
            sessionCache.put(sessions.getSessionId(), sessions);
        }
    }

    public Map<String,Sessions> persistCacheToDatabase(){
        Map<String, Sessions> oldCache = new LinkedHashMap<>(sessionCache);
        sessionCache.clear();
        return oldCache;
    }
}
