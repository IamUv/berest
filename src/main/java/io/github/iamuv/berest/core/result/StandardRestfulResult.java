package io.github.iamuv.berest.core.result;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class StandardRestfulResult extends SimpleRestfulResult {


    public static class FILED extends SimpleRestfulResult.FILED {

        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String TIMER = "timer";
        public static final String CODE = "code";

    }

    protected boolean success = true;

    protected String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    protected Error error;

    protected Timer timer;

    public StandardRestfulResult(String id) {
        super(id);
    }

    @Override
    public int getHttpStatus(ResultConfigurer configuration) {
        return super.getHttpStatus(configuration);
    }

    @Override
    public Map<String, Object> buildReturnResult(ResultConfigurer configuration, Map result) {

        if (configuration.isEnable(FILED.SUCCESS))
            result.put(FILED.SUCCESS, success);

        if (configuration.isEnable(FILED.ERROR)) {
            result.put(FILED.ERROR, error);
        }

        if (configuration.isEnable(FILED.TIMER)) {
            if (timer.getResponse() == null) {
                timer.setResponse(LocalDateTime.now());
            }
            result.put(FILED.TIMER, timer);
        }

        if (configuration.isEnable(FILED.CODE)) {
            result.put(FILED.CODE, code);
        }

        return super.buildReturnResult(configuration, result);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String message, String description, Object throwable) {
        this.error = new Error(message, description, throwable);
    }

    public void setTimer(LocalDateTime requestTime, LocalDateTime responseTime) {
        this.timer = new Timer(requestTime, responseTime);
    }

    public Error getError() {
        return error;
    }

    public Timer getTimer() {
        return timer;
    }


    class Error {

        private String message;

        private String description;

        private Object exception;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getException() {
            return exception;
        }

        public void setException(Object exception) {
            this.exception = exception;
        }

        public Error() {
        }

        public Error(String message, String description, Object exception) {
            this.message = message;
            this.description = description;
            this.exception = exception;
        }
    }

    class Timer {

        private LocalDateTime request;

        private LocalDateTime response;

        private long duration;

        public LocalDateTime getRequest() {
            return request;
        }

        public LocalDateTime getResponse() {
            return response;
        }

        public long getDuration() {
            return duration;
        }

        public void setRequest(LocalDateTime request) {
            this.request = request;
            if (response != null) {
                this.duration = response.toInstant(ZoneOffset.UTC).toEpochMilli() - request.toInstant(ZoneOffset.UTC).toEpochMilli();
            }
        }

        public void setResponse(LocalDateTime response) {
            this.response = response;
            if (request != null) {
                this.duration = response.toInstant(ZoneOffset.UTC).toEpochMilli() - request.toInstant(ZoneOffset.UTC).toEpochMilli();
            }
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public Timer() {
        }

        public Timer(LocalDateTime request, LocalDateTime response) {
            this.request = request;
            this.response = response;
            if (request != null && response != null) {
                this.duration = response.toInstant(ZoneOffset.UTC).toEpochMilli() - request.toInstant(ZoneOffset.UTC).toEpochMilli();
            }
        }

    }

    public static class Builder<T> {

        private String id;

        private int status;

        private String message;

        private T data;

        private String code;

        private String errorMessage;

        private String errorDescription;

        private Throwable exception;

        private LocalDateTime requestTime;

        private LocalDateTime responseTime;

        public Builder<T> id(String id) {
            this.id = id;
            return this;
        }

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder<T> exception(Throwable exception) {
            this.exception = exception;
            return this;
        }

        public Builder<T> errorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
            return this;
        }

        public Builder<T> requestTime(LocalDateTime requestTime) {
            this.requestTime = requestTime;
            return this;
        }

        public Builder<T> responseTime(LocalDateTime responseTime) {
            this.responseTime = responseTime;
            return this;
        }

        public StandardRestfulResult build() {
            StandardRestfulResult r = new StandardRestfulResult(id);
            r.setCode(code);
            r.setHttpStatus(status);
            r.setData(data);
            r.setMessage(message);
            r.setError(errorMessage, errorDescription, exception);
            r.setTimer(this.requestTime, this.responseTime);
            return r;
        }
    }

}
