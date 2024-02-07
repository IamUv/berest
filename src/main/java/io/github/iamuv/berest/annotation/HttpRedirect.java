package io.github.iamuv.berest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static jakarta.servlet.http.HttpServletResponse.SC_FOUND;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRedirect {

    int value() default SC_FOUND;

    String url() default "";

    boolean clearBuffer() default true;

}
