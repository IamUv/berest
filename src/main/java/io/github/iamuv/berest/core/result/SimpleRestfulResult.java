package io.github.iamuv.berest.core.result;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;

import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_MULTIPLE_CHOICES;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class SimpleRestfulResult extends RestfulResult {

    public static class FILED {
        public static final String MESSAGE = "message";
        public static final String DATA = "data";

    }

    protected Object data;

    protected String message;

    public SimpleRestfulResult(String id) {
        super(id);
    }

    @Override
    public Map<String, Object> buildReturnResult(ResultConfigurer configuration, Map<String, Object> result) {

        if (configuration.isEnable(FILED.MESSAGE)) {
            if (message == null) {
                if (httpStatus >= SC_OK && httpStatus < SC_MULTIPLE_CHOICES) {
                    message = "success";
                } else {
                    message = "failure";
                }
            }
            result.put(FILED.MESSAGE, message);
        }

        if (configuration.isEnable(FILED.DATA)) {
            result.put(FILED.DATA, data);
        }
        return result;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
