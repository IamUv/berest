package io.github.iamuv.berest.spring;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("berest")
public class SpringConfigurationProperties {

    public enum ResultMode {

        SIMPLE,
        STANDARD

    }

    private Result result = new Result();

    public class Result {

        private ResultMode mode = ResultMode.SIMPLE;

        private boolean debug = false;

        public ResultMode getMode() {
            return mode;
        }

        public void setMode(ResultMode mode) {
            this.mode = mode;
        }

        public boolean isDebug() {
            return debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public class Visible {

            private boolean id = false;

            private boolean status = false;

            public boolean isId() {
                return id;
            }

            public void setId(boolean id) {
                this.id = id;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }
        }

        private Visible visible = new Visible();


        public Visible getVisible() {
            return visible;
        }

        public void setVisible(Visible visible) {
            this.visible = visible;
        }
    }


    private HttpStatus httpStatus = new HttpStatus();

    public class HttpStatus {

        private Default Default = new Default();

        public class Default {

            private int post = HttpServletResponse.SC_CREATED;

            private int put = HttpServletResponse.SC_CREATED;

            private int delete = HttpServletResponse.SC_CREATED;

            public int getPost() {
                return post;
            }

            public void setPost(int post) {
                this.post = post;
            }

            public int getPut() {
                return put;
            }

            public void setPut(int put) {
                this.put = put;
            }

            public int getDelete() {
                return delete;
            }

            public void setDelete(int delete) {
                this.delete = delete;
            }
        }


        private ReturnVoid returnVoid = new ReturnVoid();

        public class ReturnVoid {

            private int get = HttpServletResponse.SC_OK;

            private int post = HttpServletResponse.SC_CREATED;

            private int put = HttpServletResponse.SC_RESET_CONTENT;

            private int delete = HttpServletResponse.SC_NO_CONTENT;

            public int getGet() {
                return get;
            }

            public void setGet(int get) {
                this.get = get;
            }

            public int getPost() {
                return post;
            }

            public void setPost(int post) {
                this.post = post;
            }

            public int getPut() {
                return put;
            }

            public void setPut(int put) {
                this.put = put;
            }

            public int getDelete() {
                return delete;
            }

            public void setDelete(int delete) {
                this.delete = delete;
            }
        }

        public HttpStatus.Default getDefault() {
            return Default;
        }

        public void setDefault(HttpStatus.Default aDefault) {
            Default = aDefault;
        }

        public ReturnVoid getReturnVoid() {
            return returnVoid;
        }

        public void setReturnVoid(ReturnVoid returnVoid) {
            this.returnVoid = returnVoid;
        }
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
