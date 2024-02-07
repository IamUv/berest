package io.github.iamuv.berest.core.configuration;

import io.github.iamuv.berest.core.BeRestExceptionHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class ExceptionHandlerConfigurer {

    Logger log = LoggerFactory.getLogger("BeRest.ExceptionHandlerConfigurer");

    protected BeRestExceptionHandler defaultHandler;

    protected Map<Class, BeRestExceptionHandler> handlerObserverMap;

    public Map<Class, BeRestExceptionHandler> getHandlerObserverMap() {
        return handlerObserverMap;
    }

    public List<BeRestExceptionHandler> getHandlers() {
        return new ArrayList<>(handlerObserverMap.values());
    }

    public ExceptionHandlerConfigurer registerHandler(BeRestExceptionHandler... handlers) {
        Arrays.stream(handlers).forEach(handler -> {
            if (handler != null) {
                handlerObserverMap.put(handler.getExceptionClass(), handler);
                log.debug("Exception Handler Observers add key '" + handler.getExceptionClass().getName() + "', value '" + handler.getClass().getName() + "'.");
            }
        });
        return this;
    }

    public ExceptionHandlerConfigurer removeHandler(BeRestExceptionHandler... handlers) {
        Arrays.stream(handlers).forEach(handler -> {
            if (handler != null) {
                handlerObserverMap.remove(handler.getExceptionClass());
                log.debug("Exception Handler Observers remove key '" + handler.getExceptionClass().getName() + "', value '" + handler.getClass().getName() + "'.");
            }
        });
        return this;
    }

    public ExceptionHandlerConfigurer registerHandler(List<BeRestExceptionHandler> handlers) {
        handlers.forEach(handler -> {
            if (handler != null) {
                handlerObserverMap.put(handler.getExceptionClass(), handler);
                log.debug("Exception Handler Observers add key '" + handler.getExceptionClass().getName() + "', value '" + handler.getClass().getName() + "'.");
            }
        });
        return this;
    }

    public ExceptionHandlerConfigurer removeHandler(List<BeRestExceptionHandler> handlers) {
        handlers.forEach(handler -> {
            if (handler != null) {
                handlerObserverMap.remove(handler.getExceptionClass());
                log.debug("Exception Handler Observers remove key '" + handler.getExceptionClass().getName() + "', value '" + handler.getClass().getName() + "'.");
            }
        });
        return this;
    }

    public BeRestExceptionHandler getHandler(Throwable throwable) {
        AtomicReference<BeRestExceptionHandler> r = new AtomicReference<>(handlerObserverMap.get(throwable.getClass()));
        if (r.get() != null) {
            return r.get();
        }

        boolean flag = handlerObserverMap.entrySet().stream().anyMatch(classBeRestExceptionHandlerEntry -> {
            if (classBeRestExceptionHandlerEntry.getKey().isAssignableFrom(throwable.getClass())) {
                r.set(classBeRestExceptionHandlerEntry.getValue());
                return true;
            }
            return false;
        });

        return flag ? r.get() : null;
    }


    public BeRestExceptionHandler getDefaultHandler() {
        return defaultHandler;
    }

    public ExceptionHandlerConfigurer setDefaultHandler(BeRestExceptionHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        return this;
    }

    public ExceptionHandlerConfigurer(BeRestExceptionHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.handlerObserverMap = new HashMap<>();
    }

    public ExceptionHandlerConfigurer() {
        this((request, response, method, throwable, result, configuration) -> result.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }


}
