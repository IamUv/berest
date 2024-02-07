package io.github.iamuv.berest.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass(DefaultSpringHandlerInterceptor.class)
public class DefaultWebMvcConfigurer implements WebMvcConfigurer, InitializingBean {

    protected Logger log = LoggerFactory.getLogger("BeRest.DefaultWebMvcConfigurer");

    protected DefaultSpringHandlerInterceptor defaultSpringHandlerInterceptor;

    @Autowired
    public void setDefaultSpringHandlerInterceptor(DefaultSpringHandlerInterceptor defaultSpringHandlerInterceptor) {
        this.defaultSpringHandlerInterceptor = defaultSpringHandlerInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(defaultSpringHandlerInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("DefaultWebMvcConfigurer initialization completed: Interceptor using " + defaultSpringHandlerInterceptor.getClass().getName());
    }
}
