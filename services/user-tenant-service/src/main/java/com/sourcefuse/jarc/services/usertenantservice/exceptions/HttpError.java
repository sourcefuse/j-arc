package com.sourcefuse.jarc.services.usertenantservice.exceptions;

import java.util.HashMap;

public class HttpError extends Exception {
    private int status;
    private int statusCode;
    private boolean expose;
    private HashMap<String, String> headers;

    public HttpError(String message, int status, int statusCode, boolean expose) {
        super(message);
        this.status = status;
        this.statusCode = statusCode;
        this.expose = expose;
        this.headers = new HashMap<String, String>();
    }

    public int getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean getExpose() {
        return expose;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public static HttpError createHttpError(int status, Object... rest) {
        String message = "";
        if (rest.length > 0) {
            Object first = rest[0];
            if (first instanceof String) {
                message = (String) first;
            } else if (first instanceof Throwable) {
                Throwable error = (Throwable) first;
                message = error.getMessage();
            } else if (first instanceof HashMap) {
                HashMap<String, String> headers = (HashMap<String, String>) first;
                HttpError httpError = new HttpError("", status, status, true);
                httpError.setHeaders(headers);
                return httpError;
            }
        }
        HttpError httpError = new HttpError(message, status, status, true);
        return httpError;
    }

    public static HttpError createHttpError(Object... rest) {
        return createHttpError(500, rest);
    }
}
