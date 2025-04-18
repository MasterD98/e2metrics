package org.rapid.userservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerUtil {
    Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtil.class);
    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(Exception exception) {
        logger.error(exception.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("error", exception.getMessage());
        body.put("status", 400);
        return ResponseEntity.badRequest().body(body);
    }
}
