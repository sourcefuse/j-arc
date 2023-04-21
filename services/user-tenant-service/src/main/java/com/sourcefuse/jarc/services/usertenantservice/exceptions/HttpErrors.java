package com.sourcefuse.jarc.services.usertenantservice.exceptions;

public class HttpErrors {
    public static HttpError HttpError(String message) {
        return new HttpError(message, 500, 500, true);
    }

    public static HttpError BadRequest(String message) {
        return new HttpError(message, 400, 400, true);
    }

    public static HttpError Unauthorized(String message) {
        return new HttpError(message, 401, 401, true);
    }

    public static HttpError PaymentRequired(String message) {
        return new HttpError(message, 402, 402, true);
    }

    public static HttpError Forbidden(String message) {
        return new HttpError(message, 403, 403, true);
    }

    public static HttpError NotFound(String message) {
        return new HttpError(message, 404, 404, true);
    }

    public static HttpError MethodNotAllowed(String message) {
        return new HttpError(message, 405, 405, true);
    }

    public static HttpError NotAcceptable(String message) {
        return new HttpError(message, 406, 406, true);
    }

    public static HttpError ProxyAuthenticationRequired(String message) {
        return new HttpError(message, 407, 407, true);
    }

    public static HttpError RequestTimeout(String message) {
        return new HttpError(message, 408, 408, true);
    }

    public static HttpError Conflict(String message) {
        return new HttpError(message, 409, 409, true);
    }

    public static HttpError Gone(String message) {
        return new HttpError(message, 410, 410, true);
    }

    public static HttpError LengthRequired(String message) {
        return new HttpError(message, 411, 411, true);
    }

    public static HttpError PreconditionFailed(String message) {
        return new HttpError(message, 412, 412, true);
    }
}
