package io.github.iamuv.berest.core.cache;

import io.github.iamuv.berest.core.result.RestfulResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ResultCache<T extends RestfulResult> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ResultCache instance;

    private ConcurrentMap<String, T> cache = new ConcurrentHashMap<>();

    private ResultCache() {
    }

    public static ResultCache getInstance() {
        if (instance == null)
            instance = new ResultCache();
        return instance;
    }

    public T remove(String id) {
        redirectCache.remove(id);
        requestTimeCache.remove(id);
        return cache.remove(id);
    }

    public T add(String id, T result) {
        this.cache.put(id, result);
        return result;
    }

    public T add(T result) {
        return add(result.getId(), result);
    }

    public T getResult(String id) {
        T r = cache.get(id);
        if (r == null) {
            NullPointerException ex = new NullPointerException("can not find result " + id + " in request cache");
            logger.error("can not final result in request cache, request id is  " + id, ex);
            throw ex;
        }
        return r;
    }

    private ConcurrentMap<String, RedirectEntity> redirectCache = new ConcurrentHashMap<>();


    public void putRedirect(String requestId, String url, boolean clearBuffer) {
        redirectCache.put(requestId, new RedirectEntity(url, clearBuffer));
    }

    public String getRedirectUrl(String requestId) {
        return redirectCache.get(requestId).getUrl();
    }

    public boolean isClearBuffer(String requestId) {
        return redirectCache.get(requestId).getCleanBuffer();
    }

    public boolean isRedirect(String requestId) {
        return redirectCache.containsKey(requestId);
    }


    private ConcurrentMap<String, LocalDateTime> requestTimeCache = new ConcurrentHashMap<>();

    public void putRequestTime(String requestId, LocalDateTime time) {
        requestTimeCache.put(requestId, time);
    }

    public LocalDateTime putRequestTime(String requestId) {
        LocalDateTime r = LocalDateTime.now();
        requestTimeCache.put(requestId, r);
        return r;
    }

    public LocalDateTime getRequestTime(String requestId) {
        return requestTimeCache.get(requestId);
    }


}

class RedirectEntity {
    private String url;

    private boolean cleanBuffer;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getCleanBuffer() {
        return cleanBuffer;
    }

    public void setCleanBuffer(boolean cleanBuffer) {
        this.cleanBuffer = cleanBuffer;
    }

    public RedirectEntity(String url, boolean cleanBuffer) {
        this.url = url;
        this.cleanBuffer = cleanBuffer;
    }
}
