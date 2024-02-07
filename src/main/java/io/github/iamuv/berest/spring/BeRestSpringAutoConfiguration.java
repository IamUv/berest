package io.github.iamuv.berest.spring;

import io.github.iamuv.berest.core.BeRestChainContext;
import io.github.iamuv.berest.core.BeRestHandler;
import io.github.iamuv.berest.core.cache.ResultCache;
import io.github.iamuv.berest.core.configuration.ExceptionHandlerConfigurer;
import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.generator.DefaultResultIdGenerator;
import io.github.iamuv.berest.core.generator.ResultIdGenerator;
import io.github.iamuv.berest.core.handler.SimpleContextChainHandler;
import io.github.iamuv.berest.core.handler.StandardContextChainHandler;
import io.github.iamuv.berest.core.handler.exception.SimpleInternalServerErrorHandler;
import io.github.iamuv.berest.core.handler.exception.StandardInternalServerErrorHandler;
import io.github.iamuv.berest.spring.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 *
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(BeRestChainContext.class)
@ComponentScan(basePackageClasses =
        {DefaultSpringBodyAdvice.class,
                DefaultSpringHandlerInterceptor.class,
                DefaultSpringExceptionHandler.class,
                DefaultWebMvcConfigurer.class})
public class BeRestSpringAutoConfiguration {

    protected static Logger log = LoggerFactory.getLogger("BeRest.SpringAutoConfiguration");

    @Configuration
    static class BeRestChainContextConfiguration {

        @Bean
        @ConditionalOnMissingBean
        BeRestChainContext beRestChainContext(BeRestHandler beRestHandler,
                                              ResultCache resultCache,
                                              ResultConfigurer resultConfigurer,
                                              ResultIdGenerator resultIdGenerator,
                                              ExceptionHandlerConfigurer exceptionHandlerConfigurer) {
            return new BeRestChainContext(beRestHandler, resultCache, resultConfigurer, resultIdGenerator, exceptionHandlerConfigurer);
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ResultCache.class)
    static class ResultCacheConfiguration {
        @Bean
        @Scope(SCOPE_SINGLETON)
        @ConditionalOnMissingBean
        public ResultCache resultCache() {
            return ResultCache.getInstance();
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ResultConfigurer.class)
    @EnableConfigurationProperties(SpringConfigurationProperties.class)
    static class BeRestResultConfiguration {

        @Bean
        @Scope(SCOPE_SINGLETON)
        @ConditionalOnMissingBean
        public ResultConfigurer resultConfiguration(SpringConfigurationProperties properties) {
            ResultConfigurer result = new ResultConfigurer(properties.getResult().isDebug(), properties.getResult().getVisible().isId(), properties.getResult().getVisible().isStatus());
            result.setMethodPostDefaultHttpStatus(properties.getHttpStatus().getDefault().getPost());
            result.setMethodPutDefaultHttpStatus(properties.getHttpStatus().getDefault().getPut());
            result.setMethodDeleteReturnVoidHttpStatus(properties.getHttpStatus().getReturnVoid().getDelete());
            result.setMethodPutReturnVoidHttpStatus(properties.getHttpStatus().getReturnVoid().getPut());
            result.setMethodPostReturnVoidHttpStatus(properties.getHttpStatus().getReturnVoid().getPost());
            result.setMethodGetReturnVoidHttpStatus(properties.getHttpStatus().getReturnVoid().getGet());
            result.setMethodGetReturnVoidHttpStatus(properties.getHttpStatus().getReturnVoid().getGet());
            StringBuilder configInfo = new StringBuilder("BeRest Configuration initialization completed");
            if (result.isDebug()) {
                log.warn(configInfo.append(", The debugging mode is enabled").toString());
            } else {
                log.info(configInfo.toString());
            }
            return result;
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(BeRestHandler.class)
    @EnableConfigurationProperties(SpringConfigurationProperties.class)
    static class BeRestHandlerConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public BeRestHandler beRestHandler(SpringConfigurationProperties properties) {
            BeRestHandler handler;
            if (properties.getResult().getMode() == SpringConfigurationProperties.ResultMode.SIMPLE) {
                handler = new SimpleContextChainHandler();
            } else if (properties.getResult().getMode() == SpringConfigurationProperties.ResultMode.STANDARD) {
                handler = new StandardContextChainHandler();
            } else {
                String errorMsg = "Can not find BeRestHandler of mode " + properties.getResult().getMode();
                log.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
            return handler;
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ResultIdGenerator.class)
    static class ResultIdGeneratorConfiguration {
        @Bean
        @Scope(SCOPE_SINGLETON)
        @ConditionalOnMissingBean
        public ResultIdGenerator resultIdGenerator() {
            return new DefaultResultIdGenerator();
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ExceptionHandlerConfigurer.class)
    @EnableConfigurationProperties(SpringConfigurationProperties.class)
    static class ExceptionHandlerConfiguration {
        @Bean
        @Scope(SCOPE_SINGLETON)
        @ConditionalOnMissingBean
        public ExceptionHandlerConfigurer exceptionHandlerConfigurer(SpringConfigurationProperties properties) {
            if (properties.getResult().getMode() == SpringConfigurationProperties.ResultMode.SIMPLE) {
                ExceptionHandlerConfigurer configurer = new ExceptionHandlerConfigurer(new SimpleInternalServerErrorHandler())
                        .registerHandler(new SimpleErrorResponseHandler(),
                                new SimpleBindExceptionHandler(),
                                new SimpleConstraintViolationExceptionHandler(),
                                new SimpleMethodValidationExceptionHandler(),
                                new SimpleMethodArgumentNotValidExceptionHandler());

                return configurer;
            } else if (properties.getResult().getMode() == SpringConfigurationProperties.ResultMode.STANDARD) {
                ExceptionHandlerConfigurer configurer = new ExceptionHandlerConfigurer(new StandardInternalServerErrorHandler())
                        .registerHandler(new StandardErrorResponseHandler(),
                                new StandardBindExceptionHandler(),
                                new StandardConstraintViolationExceptionHandler(),
                                new StandardMethodValidationExceptionHandler(),
                                new StandardMethodArgumentNotValidExceptionHandler());

                return configurer;
            } else {
                String errorMsg = "Can not find BeRestExceptionHandler of mode " + properties.getResult().getMode();
                log.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
        }

    }

}



