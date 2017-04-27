package es.nitaur;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Object> handleNoResultException(final NoResultException ex, final WebRequest request) {
        final ExceptionDetails detail = new ExceptionBuilder().exception(ex).httpStatus(HttpStatus.NOT_FOUND)
                .webRequest(request).build();
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(final EmptyResultDataAccessException ex,
            final WebRequest request) {
        final ExceptionDetails detail = new ExceptionBuilder().exception(ex).httpStatus(HttpStatus.NOT_FOUND)
                .webRequest(request).build();
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(final Exception ex, final WebRequest request) {
        final ExceptionDetails detail = new ExceptionBuilder().exception(ex)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).webRequest(request).build();
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
