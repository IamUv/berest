package io.github.iamuv.berest.spring;

import io.github.iamuv.berest.core.BeRestChainContext;
import io.github.iamuv.berest.core.result.RestfulResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class DefaultSpringHandlerInterceptor implements HandlerInterceptor, InitializingBean {

    protected Logger log = LoggerFactory.getLogger("BeRest.DefaultSpringHandlerInterceptor");

    protected BeRestChainContext beRestChainContext;

    @Autowired
    public void setRestfulChainContext(BeRestChainContext beRestChainContext) {
        this.beRestChainContext = beRestChainContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        beRestChainContext.requestReceived(request, response);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RestfulResult result = beRestChainContext.getResult(response);
        if (beRestChainContext.needRedirect(result.getId())) {
            beRestChainContext.doRedirect(request, response);
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        beRestChainContext.tryRemoveCache(response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        beRestChainContext.tryRemoveCache(response);
    }

    protected boolean isRestController(Object handler) {
        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod _handler = (HandlerMethod) handler;
            if (_handler.getBeanType().getAnnotation(RestController.class) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() {
        if (beRestChainContext == null) {
            ClassNotFoundException exception = new ClassNotFoundException(BeRestChainContext.class.getName());
            log.error("DefaultSpringHandlerInterceptor initialization failed", exception);
            throw new RuntimeException(exception);
        }
        log.info("DefaultSpringHandlerInterceptor initialization completed: RestfulChainContext using " + beRestChainContext.getClass().getName());
    }
}
