package de.mkristian.gwt.rails.session;

import de.mkristian.gwt.rails.caches.Cache;

public interface CacheManager {

    void addCache(Cache<?> cache);
    
    void purgeCaches();

}