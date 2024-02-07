package io.github.iamuv.berest.core.configuration;


import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

public class ResultConfigurer {

    protected boolean debug;

    protected boolean requestIdVisible;

    protected boolean httpStatusVisible;

    protected int methodGetReturnVoidHttpStatus;
    protected int methodPostReturnVoidHttpStatus;
    protected int methodDeleteReturnVoidHttpStatus;
    protected int methodPutReturnVoidHttpStatus;

    protected int methodPostDefaultHttpStatus;
    protected int methodPutDefaultHttpStatus;
    protected int methodDeleteDefaultHttpStatus;

    public ResultConfigurer setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isEnableRequestId() {
        return requestIdVisible;
    }

    public boolean isEnableHttpStatus() {
        return httpStatusVisible;
    }

    protected Set<String> ignoreFieldNames;

    public Set<String> getIgnoreFieldNameList() {
        return ignoreFieldNames;
    }

    public boolean isEnable(String fieldName) {
        if (ignoreFieldNames.contains(fieldName)) {
            return false;
        }
        return true;
    }


    protected Map<Integer, Integer> convertHttpStatusCodeMap;

    public void setConvertHttpStatusCode(int targetCode, int replaceCode) {
        convertHttpStatusCodeMap.put(targetCode, replaceCode);
    }

    public void removeConvertHttpStatusCode(int targetCode) {
        convertHttpStatusCodeMap.remove(targetCode);
    }

    public int convertHttpStatus(int statusCode) {
        if (convertHttpStatusCodeMap == null || convertHttpStatusCodeMap.size() == 0) {
            return statusCode;
        }
        Integer code = convertHttpStatusCodeMap.get(statusCode);
        return code == null ? statusCode : code.intValue();
    }

    public ResultConfigurer ignore(String fieldName) {
        if (ignoreFieldNames == null) {
            ignoreFieldNames = new HashSet<>();
        }
        if (ignoreFieldNames.size() == 0) {
            return this;
        }
        ignoreFieldNames.add(fieldName);
        return this;
    }

    public ResultConfigurer ignore(String... fieldNames) {
        if (ignoreFieldNames == null) {
            ignoreFieldNames = new HashSet<>();
        }
        if (ignoreFieldNames.size() == 0) {
            return this;
        }
        Arrays.stream(fieldNames).forEach(f -> ignore(f));
        return this;
    }

    public ResultConfigurer enable(String fieldName) {
        if (ignoreFieldNames == null) {
            ignoreFieldNames = new HashSet<>();
        }
        if (ignoreFieldNames.size() == 0) {
            return this;
        }
        ignoreFieldNames.remove(fieldName);
        return this;
    }

    public ResultConfigurer enable(String... fieldNames) {
        if (ignoreFieldNames == null) {
            ignoreFieldNames = new HashSet<>();
        }
        if (ignoreFieldNames.size() == 0) {
            return this;
        }
        Arrays.stream(fieldNames).forEach(f -> enable(f));
        return this;
    }

    public ResultConfigurer enableDebug() {
        this.debug = true;
        return this;
    }

    public ResultConfigurer disableDebug() {
        this.debug = false;
        return this;
    }

    public ResultConfigurer enableRequestIdVisible() {
        this.requestIdVisible = true;
        return this;
    }

    public ResultConfigurer disableRequestIdVisible() {
        this.requestIdVisible = false;
        return this;
    }

    public ResultConfigurer enableHttpStatusVisible() {
        this.httpStatusVisible = true;
        return this;
    }

    public ResultConfigurer disableHttpStatusVisible() {
        this.httpStatusVisible = false;
        return this;
    }

    public int getMethodGetReturnVoidHttpStatus() {
        return methodGetReturnVoidHttpStatus;
    }

    public ResultConfigurer setMethodGetReturnVoidHttpStatus(int methodGetReturnVoidHttpStatus) {
        this.methodGetReturnVoidHttpStatus = methodGetReturnVoidHttpStatus;
        return this;
    }

    public int getMethodPostReturnVoidHttpStatus() {
        return methodPostReturnVoidHttpStatus;
    }

    public ResultConfigurer setMethodPostReturnVoidHttpStatus(int methodPostReturnVoidHttpStatus) {
        this.methodPostReturnVoidHttpStatus = methodPostReturnVoidHttpStatus;
        return this;
    }

    public int getMethodDeleteReturnVoidHttpStatus() {
        return methodDeleteReturnVoidHttpStatus;
    }

    public ResultConfigurer setMethodDeleteReturnVoidHttpStatus(int methodDeleteReturnVoidHttpStatus) {
        this.methodDeleteReturnVoidHttpStatus = methodDeleteReturnVoidHttpStatus;
        return this;
    }

    public int getMethodPutReturnVoidHttpStatus() {
        return methodPutReturnVoidHttpStatus;
    }

    public ResultConfigurer setMethodPutReturnVoidHttpStatus(int methodPutReturnVoidHttpStatus) {
        this.methodPutReturnVoidHttpStatus = methodPutReturnVoidHttpStatus;
        return this;
    }

    public int getMethodPostDefaultHttpStatus() {
        return methodPostDefaultHttpStatus;
    }

    public ResultConfigurer setMethodPostDefaultHttpStatus(int methodPostDefaultHttpStatus) {
        this.methodPostDefaultHttpStatus = methodPostDefaultHttpStatus;
        return this;
    }

    public int getMethodPutDefaultHttpStatus() {
        return methodPutDefaultHttpStatus;
    }

    public ResultConfigurer setMethodPutDefaultHttpStatus(int methodPutDefaultHttpStatus) {
        this.methodPutDefaultHttpStatus = methodPutDefaultHttpStatus;
        return this;
    }

    public int getMethodDeleteDefaultHttpStatus() {
        return methodDeleteDefaultHttpStatus;
    }

    public ResultConfigurer setMethodDeleteDefaultHttpStatus(int methodDeleteDefaultHttpStatus) {
        this.methodDeleteDefaultHttpStatus = methodDeleteDefaultHttpStatus;
        return this;
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible, boolean httpStatusVisible, int methodGetReturnVoidHttpStatus, int methodPostReturnVoidHttpStatus, int methodDeleteReturnVoidHttpStatus, int methodPutReturnVoidHttpStatus, int methodPostDefaultHttpStatus, int methodPutDefaultHttpStatus, int methodDeleteDefaultHttpStatus, Set<String> ignoreFieldNames, Map<Integer, Integer> convertHttpStatusCodeMap) {
        this.debug = debug;
        this.requestIdVisible = requestIdVisible;
        this.httpStatusVisible = httpStatusVisible;
        this.methodGetReturnVoidHttpStatus = methodGetReturnVoidHttpStatus;
        this.methodPostReturnVoidHttpStatus = methodPostReturnVoidHttpStatus;
        this.methodDeleteReturnVoidHttpStatus = methodDeleteReturnVoidHttpStatus;
        this.methodPutReturnVoidHttpStatus = methodPutReturnVoidHttpStatus;
        this.methodPostDefaultHttpStatus = methodPostDefaultHttpStatus;
        this.methodPutDefaultHttpStatus = methodPutDefaultHttpStatus;
        this.methodDeleteDefaultHttpStatus = methodDeleteDefaultHttpStatus;
        this.ignoreFieldNames = ignoreFieldNames;
        this.convertHttpStatusCodeMap = convertHttpStatusCodeMap;
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible, boolean httpStatusVisible, int methodGetReturnVoidHttpStatus, int methodPostReturnVoidHttpStatus, int methodDeleteReturnVoidHttpStatus, int methodPutReturnVoidHttpStatus, int methodPostDefaultHttpStatus, int methodPutDefaultHttpStatus, int methodDeleteDefaultHttpStatus, Set<String> ignoreFieldNames) {
        this(debug, requestIdVisible, httpStatusVisible, methodGetReturnVoidHttpStatus, methodPostReturnVoidHttpStatus, methodDeleteReturnVoidHttpStatus, methodPutReturnVoidHttpStatus, methodPostDefaultHttpStatus, methodPutDefaultHttpStatus, methodDeleteDefaultHttpStatus, ignoreFieldNames, new HashMap<>());
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible, boolean httpStatusVisible, int methodGetReturnVoidHttpStatus, int methodPostReturnVoidHttpStatus, int methodDeleteReturnVoidHttpStatus, int methodPutReturnVoidHttpStatus, int methodPostDefaultHttpStatus, int methodPutDefaultHttpStatus, int methodDeleteDefaultHttpStatus, boolean ignoreNullField) {
        this(debug, requestIdVisible, httpStatusVisible, methodGetReturnVoidHttpStatus, methodPostReturnVoidHttpStatus, methodDeleteReturnVoidHttpStatus, methodPutReturnVoidHttpStatus, methodPostDefaultHttpStatus, methodPutDefaultHttpStatus, methodDeleteDefaultHttpStatus, new HashSet<>());
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible, boolean httpStatusVisible, int methodGetReturnVoidHttpStatus, int methodPostReturnVoidHttpStatus, int methodDeleteReturnVoidHttpStatus, int methodPutReturnVoidHttpStatus, int methodPostDefaultHttpStatus, int methodPutDefaultHttpStatus, int methodDeleteDefaultHttpStatus) {
        this(debug, requestIdVisible, httpStatusVisible, methodGetReturnVoidHttpStatus, methodPostReturnVoidHttpStatus, methodDeleteReturnVoidHttpStatus, methodPutReturnVoidHttpStatus, methodPostDefaultHttpStatus, methodPutDefaultHttpStatus, methodDeleteDefaultHttpStatus, true);
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible, boolean httpStatusVisible, int methodGetReturnVoidHttpStatus, int methodPostReturnVoidHttpStatus, int methodDeleteReturnVoidHttpStatus, int methodPutReturnVoidHttpStatus) {
        this(debug, requestIdVisible, httpStatusVisible, methodGetReturnVoidHttpStatus, methodPostReturnVoidHttpStatus, methodDeleteReturnVoidHttpStatus, methodPutReturnVoidHttpStatus, HttpServletResponse.SC_OK, HttpServletResponse.SC_OK, HttpServletResponse.SC_OK);
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible, boolean httpStatusVisible) {
        this(debug, requestIdVisible, httpStatusVisible, HttpServletResponse.SC_OK, HttpServletResponse.SC_OK, HttpServletResponse.SC_OK, HttpServletResponse.SC_OK);
    }

    public ResultConfigurer(boolean debug, boolean requestIdVisible) {
        this(debug, requestIdVisible, false);
    }

    public ResultConfigurer(boolean debug) {
        this(debug, false);
    }

    public ResultConfigurer() {
        this(false);
    }
}
